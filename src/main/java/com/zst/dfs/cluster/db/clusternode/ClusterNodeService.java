package com.zst.dfs.cluster.db.clusternode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Duration;

@Service
public class ClusterNodeService {
    @Autowired
    private ClusterNodeMapper clusterNodeMapper;

    /**
     * 注册节点数据
     * @param nodeId
     * @param host
     */
    public void registerNode(String nodeId, String host) {
        ClusterNode node = clusterNodeMapper.selectById(nodeId);
        if (node == null) {
            node = new ClusterNode();
            node.setId(nodeId);
            node.setHost(host);
            node.setHeartbeatTime(System.currentTimeMillis());
            clusterNodeMapper.insert(node);
        } else {
            node.setHeartbeatTime(System.currentTimeMillis());
            clusterNodeMapper.updateById(node);
        }
    }

    public void updateHeartbeat(String nodeId) {
        ClusterNode node = clusterNodeMapper.selectById(nodeId);
        if (node == null) {
            throw new RuntimeException("node not found");
        }

        node.setHeartbeatTime(System.currentTimeMillis());
        clusterNodeMapper.updateById(node);
    }

    public ClusterNode getNode(String nodeId) {
        ClusterNode node = clusterNodeMapper.selectById(nodeId);
        // TODO 这里需要改成可配置的超时时间
        if (node.getHeartbeatTime() < System.currentTimeMillis() - Duration.ofSeconds(30).toMillis()) {
            return node;
        }
        return null;
    }
}
