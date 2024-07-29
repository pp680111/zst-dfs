package com.zst.dfs.cluster.db;

import com.zst.dfs.cluster.db.clusternode.ClusterNodeService;
import com.zst.dfs.cluster.db.filereplica.FileReplicaService;
import com.zst.dfs.storage.metadata.MetadataService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClusterNodeManager {
    private ClusterNodeService clusterNodeService;
    private FileReplicaService fileReplicaService;
    private MetadataService metadataService;
    private NodeProperties nodeProperties;

    private ScheduledExecutorService heartbeatExecutor;

    public ClusterNodeManager(ClusterNodeService clusterNodeService,
                              FileReplicaService fileReplicaService,
                              MetadataService metadataService,
                              NodeProperties nodeProperties) {
        if (clusterNodeService == null || fileReplicaService == null || metadataService == null || nodeProperties == null) {
            throw new IllegalArgumentException("ClusterNodeManager constructor arguments cannot be null");
        }
        this.clusterNodeService = clusterNodeService;
        this.fileReplicaService = fileReplicaService;
        this.metadataService = metadataService;
        this.nodeProperties = nodeProperties;

        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        init();
    }

    public void init() {
        clusterNodeService.registerNode(nodeProperties.getId(), nodeProperties.getAddress());
        scheduleHeartbeat();
    }

    private void scheduleHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            clusterNodeService.updateHeartbeat(nodeProperties.getId());
        }, 5, 5, TimeUnit.SECONDS);
    }
}
