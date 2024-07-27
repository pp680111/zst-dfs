package com.zst.dfs.storage.metadata.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("file_metadata")
public class DbFileMetadata {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String originalName;
    private String fileName;
    private Long fileSize;
    private String sign;
    private String properties;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean deleted;
}
