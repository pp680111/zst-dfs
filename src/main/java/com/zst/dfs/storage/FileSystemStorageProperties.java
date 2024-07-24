package com.zst.dfs.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "storage.file-system")
public class FileSystemStorageProperties {
    /**
     * 文件存储路径
     */
    private String basePath;
}
