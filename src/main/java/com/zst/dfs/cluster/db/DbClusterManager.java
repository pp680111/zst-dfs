package com.zst.dfs.cluster.db;

import com.zst.dfs.cluster.db.filereplica.FileReplicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbClusterManager {
    @Autowired
    private ClusterNodeManager clusterNodeManager;
    @Autowired
    private FileReplicaService fileReplicaService;
}
