package com.zst.dfs.utils;

import org.springframework.util.StringUtils;

import java.net.URLConnection;

public class FileUtils {
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    /**
     * 根据文件名获取MIME类型
     * @param fileName
     * @return
     */
    public static String getMimeType(String fileName) {
        if (fileName == null || StringUtils.isEmpty(fileName)) {
            return null;
        }

        String mimeType =  URLConnection.getFileNameMap().getContentTypeFor(fileName);
        if (mimeType == null) {
            mimeType = DEFAULT_MIME_TYPE;
        }

        return mimeType;
    }
}
