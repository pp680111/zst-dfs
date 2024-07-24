package com.zst.dfs.storage;

import java.io.File;

public interface StorageService {
    /**
     * 保存文件
     * @param file
     */
    FileMetadata save(File file, String originalName);

//    FileMetadata append(InputStream inputStream, long start, long end, boolean ifEnd);

    File getFile(String id);
}
