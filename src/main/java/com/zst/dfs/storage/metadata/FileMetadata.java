package com.zst.dfs.storage.metadata;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class FileMetadata {
    private String id;
    private String originalName;
    private String fileName;
    private long fileSize;
    private String sign;
    private Map<String, String> properties;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean deleted = false;

    public byte[] serialize() {
        return JSON.toJSONString(this).getBytes(StandardCharsets.UTF_8);
    }

    public static FileMetadata fromBytes(byte[] serializeBytes) {
        if (serializeBytes == null) {
            return null;
        }

        String json = new String(serializeBytes, StandardCharsets.UTF_8);
        return JSON.parseObject(json, FileMetadata.class);
    }
}
