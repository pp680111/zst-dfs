package com.zst.dfs.cluster.db;

import com.zst.dfs.cluster.db.clusternode.ClusterNodeService;
import com.zst.dfs.cluster.db.filereplica.FileReplicaService;
import com.zst.dfs.storage.metadata.MetadataService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({NodeProperties.class})
public class DbClusterConfiguration {

    @Bean
    public ClusterNodeManager clusterNodeManager(ClusterNodeService clusterNodeService,
                                                 FileReplicaService fileReplicaService,
                                                 MetadataService metadataService,
                                                 NodeProperties nodeProperties) {
        return new ClusterNodeManager(clusterNodeService, fileReplicaService, metadataService, nodeProperties);
    }
}
