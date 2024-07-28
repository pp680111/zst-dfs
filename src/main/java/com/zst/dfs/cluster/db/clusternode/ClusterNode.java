package com.zst.dfs.cluster.db.clusternode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClusterNode {
    /**
     * 节点id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 最后心跳时间
     */
    private Long heartbeatTime;
    /**
     * 节点ip
     */
    private String host;
}
