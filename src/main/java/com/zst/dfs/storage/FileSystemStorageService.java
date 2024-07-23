package com.zst.dfs.storage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class FileSystemStorageService implements StorageService {
    private MetadataService metadataService;

    @Override
    public String save(File file) {
        /*
            step:
            1，生成metadata
            2，根据metadata计算要存储的位置
            3，保存metadata
            4，移动文件
         */
        FileMetadata metadata = createMetadataForFile(file, file.getName());

        return null;
    }

    @Override
    public File getFile(String id) {
        return null;
    }

    private FileMetadata createMetadataForFile(File file, String fileName) {
        FileMetadata metadata = new FileMetadata();
        metadata.setId(UUID.randomUUID().toString().replace("-", ""));
        metadata.setOriginalName(fileName);
        metadata.setFileName(file.getName());
        metadata.setFileSize(file.length());
        metadata.setCreateTime(LocalDateTime.now());
        metadata.setUpdateTime(LocalDateTime.now());
        metadata.setProperties(null);
        return metadata;
    }

    private String getFileStoragePath(FileMetadata metadata) {

    }
}
