package com.zst.dfs.cluster.db.filereplica;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FileReplicaService {
    @Autowired
    private FileReplicaMapper fileReplicaMapper;

    public Long addFileReplica(String metadataId, String nodeId) {
        FileReplica fileReplica = new FileReplica();
        fileReplica.setMetadataId(metadataId);
        fileReplica.setNodeId(nodeId);
        fileReplica.setCreateTime(LocalDateTime.now());
        fileReplicaMapper.insert(fileReplica);

        return fileReplica.getId();
    }

    public FileReplica getFileReplica(String metadataId, String nodeId) {
        if (metadataId == null || nodeId == null) {
            throw new RuntimeException("metadataId and nodeId must not be null");
        }

        return fileReplicaMapper.selectOne(Wrappers.query(FileReplica.class)
                .eq("metadata_id", metadataId)
                .eq("node_id", nodeId));
    }
}
