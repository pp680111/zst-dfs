package com.zst.dfs.storage;

import com.zst.dfs.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileSystemStorageService implements StorageService {
    @Autowired
    private FileSystemStorageProperties properties;
    @Autowired
    private MetadataService metadataService;

    @Override
    public FileMetadata save(File file, String originalName) {
        /*
            step:
            1，生成metadata
            2，根据metadata计算要存储的位置
            3，保存metadata
            4，移动文件
         */
        try {
            FileMetadata metadata = createMetadataForFile(file, originalName);
            Path fileStoragePath = getFileStoragePath(metadata);
            if (!Files.exists(fileStoragePath.getParent())) {
                Files.createDirectories(fileStoragePath.getParent());
            }

            metadataService.updateMetadata(metadata);
            Files.move(file.toPath(), fileStoragePath, StandardCopyOption.REPLACE_EXISTING);

            return metadata;
        } catch (Exception e) {
            throw new StorageException("Failed to store file " + file.getName(), e);
        }
    }

    @Override
    public File getFile(String id) {
        FileMetadata metadata = metadataService.getFileMetadata(id);
        if (metadata == null) {
            throw new StorageException("File not found");
        }
        Path fileStoragePath = getFileStoragePath(metadata);
        return fileStoragePath.toFile();
    }

    @Override
    public FileMetadata getMetadata(String id) {
        return metadataService.getFileMetadata(id);
    }

    private FileMetadata createMetadataForFile(File file, String fileName) {
        FileMetadata metadata = new FileMetadata();
        metadata.setId(UUID.randomUUID().toString().replace("-", ""));
        metadata.setOriginalName(fileName);

        if (fileName.lastIndexOf(".") != -1) {
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            metadata.setFileName(metadata.getId() + "." + fileExtension);
        } else {
            metadata.setFileName(metadata.getId());
        }

        metadata.setFileSize(file.length());
        metadata.setCreateTime(LocalDateTime.now());
        metadata.setUpdateTime(LocalDateTime.now());
        metadata.setProperties(null);
        return metadata;
    }

    /**
     * 获取文件存储路径
     * @param metadata
     * @return
     */
    private Path getFileStoragePath(FileMetadata metadata) {
        String[] layerPaths = getLayerPaths(metadata.getId());
        List<String> pathElements = new ArrayList<>();
        pathElements.addAll(List.of(layerPaths));
        pathElements.add(metadata.getFileName());

        return Paths.get(properties.getBasePath(), pathElements.toArray(new String[0]));
    }

    /**
     * 获取文件存储层级路径
     * @param fileId
     * @return
     */
    private String[] getLayerPaths(String fileId) {
        return new String[] {fileId.substring(0, 2), fileId.substring(2, 4)};
    }
}
