package com.zst.dfs.cluster.db;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.dfs.cluster.db.clusternode.ClusterNode;
import com.zst.dfs.cluster.db.clusternode.ClusterNodeService;
import com.zst.dfs.cluster.db.filereplica.FileReplica;
import com.zst.dfs.cluster.db.filereplica.FileReplicaService;
import com.zst.dfs.storage.StorageService;
import com.zst.dfs.storage.metadata.FileMetadata;
import com.zst.dfs.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ClusterNodeManager {
    private ClusterNodeService clusterNodeService;
    private FileReplicaService fileReplicaService;
    private StorageService storageService;
    private NodeProperties nodeProperties;
    private ScheduledExecutorService heartbeatExecutor;
    private ExecutorService fileSyncScheduler;
    private Thread fileSyncThread;
    private RestTemplate restTemplate = new RestTemplate();

    public ClusterNodeManager(ClusterNodeService clusterNodeService,
                              FileReplicaService fileReplicaService,
                              StorageService storageService,
                              NodeProperties nodeProperties) {
        if (clusterNodeService == null || fileReplicaService == null || storageService == null || nodeProperties == null) {
            throw new IllegalArgumentException("ClusterNodeManager constructor arguments cannot be null");
        }
        this.clusterNodeService = clusterNodeService;
        this.fileReplicaService = fileReplicaService;
        this.storageService = storageService;
        this.nodeProperties = nodeProperties;

        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        fileSyncScheduler = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        init();
    }

    public void init() {
        clusterNodeService.registerNode(nodeProperties.getId(), nodeProperties.getAddress());
        scheduleHeartbeat();
        fileSyncThread = new Thread(this::scheduleFileSync);
        fileSyncThread.start();
    }

    public void addNodeFileReplica(String metadataId) {
        // 如果该文件元数据在当前节点上已有副本的话，则不做处理
        if (fileReplicaService.getFileReplica(metadataId, nodeProperties.getId()) != null) {
            return;
        }

        fileReplicaService.addFileReplica(metadataId, nodeProperties.getId());
    }

    private void scheduleHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            clusterNodeService.updateHeartbeat(nodeProperties.getId());
        }, 5, 5, TimeUnit.SECONDS);
    }

    private void scheduleFileSync() {
        while (true) {
            try {
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));

                Page<FileReplica> fileReplicas = fileReplicaService.queryNodeUnsyncFiles(nodeProperties.getId(), 1, 10);
                List<FileReplica> records = fileReplicas.getRecords();
                if (records.size() == 0) {
                    log.info("节点待同步文件为空， 跳过同步流程");
                    return;
                }

                List<String> metadataIds = records.stream().map(FileReplica::getMetadataId).distinct().toList();
                List<CompletableFuture<Void>> syncTasks = metadataIds.stream().map(metadataId -> {
                    return CompletableFuture.runAsync(() -> this.syncFile(metadataId), fileSyncScheduler);
                }).toList();

                CompletableFuture.allOf(syncTasks.toArray(new CompletableFuture[0])).join();
            } catch (Exception e) {
                log.error("同步文件调度线程执行出错", e);
            }

        }
    }

    private void syncFile(String metadataId) {
        FileMetadata fileMetadata = storageService.getMetadata(metadataId);
        if (fileMetadata == null) {
            log.error("文件元数据 {} 不存在，无法同步", metadataId);
            return;
        }
        log.info("开始同步文件元数据 {}", metadataId);

        List<FileReplica> fileReplicas = fileReplicaService.getFileReplicas(metadataId);
        if (fileReplicas.isEmpty()) {
            log.error("文件元数据 {} 未找到可用副本，无法同步", metadataId);
            return;
        }

        for (FileReplica fileReplica : fileReplicas) {
            if (fileReplica.getNodeId().equals(nodeProperties.getId())) {
                continue;
            }

            ClusterNode node = clusterNodeService.getNode(fileReplica.getNodeId());
            if (node == null) {
                log.error("节点 {} 不存在，无法同步文件，跳过", fileReplica.getNodeId());
                continue;
            }

            String downloadUrl = buildDownloadUrl(node, metadataId);

            try {
                restTemplate.execute(downloadUrl, HttpMethod.GET, null, response -> {
                    ContentDisposition contentDisposition = response.getHeaders().getContentDisposition();
                    String fileName = UUID.randomUUID().toString();

                    String tmpDir = System.getProperty("java.io.tmpdir");
                    Path tmpFilePath = Paths.get(tmpDir, fileName);

                    Files.copy(response.getBody(), tmpFilePath);
                    File file = tmpFilePath.toFile();

                    if (!file.exists()) {
                        throw new RuntimeException("下载的文件不存在， " + tmpFilePath.toString());
                    }

                    String syncedFileDigest = FileUtils.getMD5Digest(file);
                    if (!syncedFileDigest.equals(fileMetadata.getSign())) {
                        throw new RuntimeException(MessageFormat.format("同步的文件的签名摘要不一致，" +
                                "生成的签名摘要=｛0｝,原文件签名摘要={1}，本次同步无效",
                                syncedFileDigest, fileMetadata.getSign()));
                    }

                    storageService.saveFileFromSync(file, fileMetadata);
                    fileReplicaService.addFileReplica(metadataId, nodeProperties.getId());
                    log.info("完成对id={}文件的同步", metadataId);
                    return null;
                });
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private String buildDownloadUrl(ClusterNode node, String metadataId) {
        return String.format("http://%s/download?id=%s", node.getHost(), metadataId);
    }
}
