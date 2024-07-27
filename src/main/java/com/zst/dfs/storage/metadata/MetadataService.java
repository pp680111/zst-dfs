package com.zst.dfs.storage.metadata;

import com.zst.dfs.storage.metadata.FileMetadata;

public interface MetadataService {
    /**
     * 获取指定文件id的元数据
     * @param id
     * @return
     */
    FileMetadata getFileMetadata(String id);

    /**
     * 更新元数据
     * @param metadata
     */
    void updateMetadata(FileMetadata metadata);
}
