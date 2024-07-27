package com.zst.dfs.controller;

import com.zst.dfs.exception.StorageException;
import com.zst.dfs.storage.metadata.FileMetadata;
import com.zst.dfs.storage.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class FileController {

    private Path uploadPath = Paths.get("d:\\", "J2EE", "zst-dfs", "upload");

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload")
    public FileMetadata upload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        File dest = Paths.get(uploadPath.toFile().getAbsolutePath(), UUID.randomUUID().toString()).toFile();

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        file.transferTo(dest);

        FileMetadata metadata = storageService.save(dest, fileName);
        return metadata;
    }

    /**
     * 查询指定id的文件元数据
     * @param id
     * @return
     */
    @GetMapping("/metadata")
    public FileMetadata getMetadata(@RequestParam("id") String id) {
        return storageService.getMetadata(id);
    }

    @GetMapping("/download")
    public void download(@RequestParam("id") String id, HttpServletResponse response) {
        File file = storageService.getFile(id);
        if (!file.exists()) {
            throw new StorageException("file not exists");
        }

        FileMetadata metadata = storageService.getMetadata(id);
        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(metadata.getOriginalName(), StandardCharsets.UTF_8));

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             OutputStream os = response.getOutputStream();) {
            byte[] buffer = new byte[1 * 1024 * 1024];
            int readBytes = 0;
            while ((readBytes = bis.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
            os.flush();
        } catch (Exception e) {
            throw new StorageException("download error", e);
        }
    }
}
