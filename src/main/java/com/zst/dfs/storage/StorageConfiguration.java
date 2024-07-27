package com.zst.dfs.storage;

import com.zst.dfs.storage.metadata.MetadataService;
import com.zst.dfs.storage.metadata.filesystem.FileSystemMetadataService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(FileSystemStorageProperties.class)
@Configuration
public class StorageConfiguration {
    @Bean
    public MetadataService metadataService() {
        return new FileSystemMetadataService();
    }

    @Bean
    public StorageService storageService() {
        return new FileSystemStorageService();
    }
}
