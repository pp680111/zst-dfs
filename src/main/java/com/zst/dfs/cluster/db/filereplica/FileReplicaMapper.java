package com.zst.dfs.cluster.db.filereplica;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileReplicaMapper extends BaseMapper<FileReplica> {
    List<String> queryUnsyncFileMetadataId(@Param("nodeId") String nodeId);
}
