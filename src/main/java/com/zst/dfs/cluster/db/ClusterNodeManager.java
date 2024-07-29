package com.zst.dfs.cluster.db;

import com.zst.dfs.cluster.db.clusternode.ClusterNodeService;
import com.zst.dfs.cluster.db.filereplica.FileReplicaService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClusterNodeManager {
    private ClusterNodeService clusterNodeService;
    private FileReplicaService fileReplicaService;
    private NodeProperties nodeProperties;

    private ScheduledExecutorService heartbeatExecutor;

    public ClusterNodeManager(ClusterNodeService clusterNodeService,
                              FileReplicaService fileReplicaService,
                              NodeProperties nodeProperties) {
        if (clusterNodeService == null || fileReplicaService == null || nodeProperties == null) {
            throw new IllegalArgumentException("ClusterNodeManager constructor arguments cannot be null");
        }
        this.clusterNodeService = clusterNodeService;
        this.fileReplicaService = fileReplicaService;
        this.nodeProperties = nodeProperties;

        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        init();
    }

    public void init() {
        clusterNodeService.registerNode(nodeProperties.getId(), nodeProperties.getAddress());
        scheduleHeartbeat();
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
}
