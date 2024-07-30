package com.zst.dfs.cluster.db.filereplica;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<FileReplica> getFileReplicas(String metadataId) {
        return fileReplicaMapper.selectList(Wrappers.query(FileReplica.class)
                .eq("metadata_id", metadataId));
    }

    public Page<FileReplica> queryNodeUnsyncFiles(String nodeId, int pageNum, int pageSize) {
        Page<FileReplica> page = Page.of(pageNum, pageSize);
        fileReplicaMapper.selectPage(page, Wrappers.query(FileReplica.class)
                .not(w -> w.eq("node_id", nodeId)));
        return page;
    }
}
