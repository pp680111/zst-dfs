package com.zst.dfs.utils;

import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.security.MessageDigest;

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

    public static String getMD5Digest(File file) {
        if (!file.exists()) {
            return "";
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);) {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }

            StringBuilder sb = new StringBuilder();
            for (byte b : md.digest()) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
         } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
