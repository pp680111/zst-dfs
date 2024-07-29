package com.zst.dfs.cluster.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "storage.node")
public class NodeProperties {
    private String id;
    private String address;
}
