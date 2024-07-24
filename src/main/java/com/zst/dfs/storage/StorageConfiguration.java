package com.zst.dfs.storage;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(FileSystemStorageProperties.class)
@Configuration
public class StorageConfiguration {
    @Bean
    public MetadataService metadataService() {
        return new MetadataService();
    }

    @Bean
    public StorageService storageService() {
        return new FileSystemStorageService();
    }
}
