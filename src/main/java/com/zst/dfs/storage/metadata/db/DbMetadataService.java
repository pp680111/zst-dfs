package com.zst.dfs.storage.metadata.db;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONValidator;
import com.zst.dfs.storage.metadata.FileMetadata;
import com.zst.dfs.storage.metadata.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

public class DbMetadataService implements MetadataService {
    @Autowired
    private DbFileMetadataMapper dbFileMetadataMapper;

    @Override
    public FileMetadata getFileMetadata(String id) {
    }

    @Override
    public void updateMetadata(FileMetadata metadata) {

    }

    /**
     * 将数据库中的DbFileMetadata实体转换为FileMetadata
     * @param dbFileMetadata
     * @return
     */
    private FileMetadata mapToFileMetadata(DbFileMetadata dbFileMetadata) {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setId(dbFileMetadata.getId());
        fileMetadata.setFileName(dbFileMetadata.getFileName());
        fileMetadata.setFileSize(Optional.ofNullable(dbFileMetadata.getFileSize()).orElse(0L));
        fileMetadata.setOriginalName(dbFileMetadata.getOriginalName());
        fileMetadata.setSign(dbFileMetadata.getSign());
        fileMetadata.setCreateTime(dbFileMetadata.getCreateTime());
        fileMetadata.setUpdateTime(dbFileMetadata.getUpdateTime());
        fileMetadata.setDeleted(Optional.ofNullable(dbFileMetadata.getDeleted()).orElse(false));

        if (dbFileMetadata.getProperties() != null
                && JSONValidator.from(dbFileMetadata.getProperties()).validate()) {
            fileMetadata.setProperties(JSONObject.parseObject(dbFileMetadata.getProperties(), Map.class));
        }

        return fileMetadata;
    }

    /**
     * 将FileMetadata转换为DbFileMetadata
     * @param fileMetadata
     * @return
     */
    private DbFileMetadata mapToDbFileMetadata(FileMetadata fileMetadata) {
        DbFileMetadata dbFileMetadata = new DbFileMetadata();
        dbFileMetadata.setId(fileMetadata.getId());
        dbFileMetadata.setFileName(fileMetadata.getFileName());
        dbFileMetadata.setFileSize(fileMetadata.getFileSize());
        dbFileMetadata.setOriginalName(fileMetadata.getOriginalName());
        dbFileMetadata.setSign(fileMetadata.getSign());
        dbFileMetadata.setCreateTime(fileMetadata.getCreateTime());
        dbFileMetadata.setUpdateTime(fileMetadata.getUpdateTime());
        dbFileMetadata.setDeleted(fileMetadata.isDeleted());

        if (fileMetadata.getProperties() != null) {
            dbFileMetadata.setProperties(JSONObject.toJSONString(fileMetadata.getProperties()));
        }
    }
}
