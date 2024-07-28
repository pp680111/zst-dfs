package com.zst.dfs.cluster.db.filereplica;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("cluster_file_replica")
public class FileReplica {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 文件元数据id
     */
    private String metadataId;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
