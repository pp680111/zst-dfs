package com.zst.dfs.storage;

import java.io.File;

public interface StorageService {
    /**
     * 保存文件
     * @param file
     */
    String save(File file);

    File getFile(String id);
}
