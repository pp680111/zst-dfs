package com.zst.dfs.controller;

import com.zst.dfs.storage.FileMetadata;
import com.zst.dfs.storage.MetadataService;
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

    @GetMapping("/download")
    public void download(@RequestParam("name") String name, HttpServletResponse response) {
        File file = Paths.get(uploadPath.toFile().getAbsolutePath(), name).toFile();
        if (!file.exists()) {
            // TODO 这里看看怎么直接返回404
            throw new RuntimeException("file not exists");
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + name);

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             OutputStream os = response.getOutputStream();) {
            byte[] buffer = new byte[1 * 1024 * 1024];
            while (bis.read(buffer) != -1) {
                os.write(buffer);
            }
            os.flush();
        } catch (Exception e) {

        }
    }
}
