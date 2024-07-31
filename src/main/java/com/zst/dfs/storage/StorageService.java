package com.zst.dfs.storage;

import com.zst.dfs.storage.metadata.FileMetadata;

import java.io.File;

public interface StorageService {
    /**
     * 保存文件
     * @param file
     */
    FileMetadata save(File file, String originalName);

    /**
     * 保存集群同步机制同步过来的文件
     * @param file
     */
    void saveFileFromSync(File file, FileMetadata fileMetadata);

    File getFile(String id);

    FileMetadata getMetadata(String id);
}
