package com.zst.dfs.cluster.configuration;

import com.zst.dfs.cluster.db.ClusterNodeManager;
import com.zst.dfs.cluster.db.NodeProperties;
import com.zst.dfs.cluster.db.clusternode.ClusterNodeService;
import com.zst.dfs.cluster.db.filereplica.FileReplicaService;
import com.zst.dfs.storage.FileSystemStorageProperties;
import com.zst.dfs.storage.FileSystemStorageService;
import com.zst.dfs.storage.StorageService;
import com.zst.dfs.storage.metadata.MetadataService;
import com.zst.dfs.storage.metadata.db.DbMetadataService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({NodeProperties.class, FileSystemStorageProperties.class})
public class DfsConfiguration {

    @Bean
    public MetadataService metadataService() {
        return new DbMetadataService();
    }

    @Bean
    public StorageService storageService() {
        return new FileSystemStorageService();
    }

    @Bean
    public ClusterNodeManager clusterNodeManager(ClusterNodeService clusterNodeService,
                                                 FileReplicaService fileReplicaService,
                                                 MetadataService metadataService,
                                                 NodeProperties nodeProperties) {
        return new ClusterNodeManager(clusterNodeService, fileReplicaService, metadataService, nodeProperties);
    }
}
