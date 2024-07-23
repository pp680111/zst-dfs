package com.zst.dfs.storage;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 管理文件元数据的类
 */
public class MetadataService {
    private MappedByteBuffer fileBuffer;
    private Map<String, FileMetadata> metadataMap = new LinkedHashMap<>();

    public MetadataService() {
        init();
    }

    public FileMetadata getFileMetadata(String id) {
        return metadataMap.get(id);
    }

    public void updateMetadata(FileMetadata metadata) {
        metadataMap.put(metadata.getId(), metadata);
    }

    private void init() {
        prepareByteBuffer();
        initMetadataMap();
    }

    private void prepareByteBuffer() {
        try {
            Path filePath = Path.of(".", "metadata.dat");
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            }

            FileChannel fileChannel = (FileChannel) Files.newByteChannel(filePath, StandardOpenOption.READ, StandardOpenOption.WRITE);
            this.fileBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 100 * 1024 * 1024);
        } catch (Exception e) {
            throw new RuntimeException("初始化metadata持久化文件失败", e);
        }
    }

    private void initMetadataMap() {
        while (true) {
            int length = fileBuffer.getInt();
            if (length == 0) {
                break;
            }

            byte[] buffer = new byte[length];
            fileBuffer.get(buffer);

            FileMetadata metadata = FileMetadata.fromBytes(buffer);
            metadataMap.put(metadata.getId(), metadata);
        }
    }
}
