package com.zst.dfs.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtilsTest {

    @Test
    public void testGetMimeTypeForJpgFile() {
        String mimeType = FileUtils.getMimeType("test.jpg");
        Assertions.assertEquals("image/jpeg", mimeType);
    }

    @Test
    public void testGetMimeTypeForTxtFile() {
        String mimeType = FileUtils.getMimeType("test.txt");
        Assertions.assertEquals("text/plain", mimeType);
    }

    @Test
    public void testGetMimeTypeForUnknownFile() {
        String mimeType = FileUtils.getMimeType("unknown.file");
        Assertions.assertEquals("application/octet-stream", mimeType);
    }

    @Test
    public void testGetMimeTypeForEmptyFileName() {
        String mimeType = FileUtils.getMimeType("");
        Assertions.assertNull(mimeType); // 假设对于空文件名，返回null。这取决于实际的方法实现。
    }

    @Test
    public void testGetMimeTypeForNullFileName() {
        String mimeType = FileUtils.getMimeType(null);
        Assertions.assertNull(mimeType); // 假设对于null文件名，返回null。这取决于实际的方法实现。
    }

    @Test
    public void testGetMD5Digest() {
        Path p = Paths.get(".", "zst.out");
        System.err.println(FileUtils.getMD5Digest(p.toFile()));

    }
}
