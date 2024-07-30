package com.zst.dfs.cluster.db.filereplica;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileReplicaService_integrateTest {
    @Autowired
    private FileReplicaService fileReplicaService;

    @Test
    public void queryNodeUnsyncFiles_test() {
        Page<FileReplica> page = fileReplicaService.queryNodeUnsyncFiles("node1", 1, 10);
        System.err.println(JSON.toJSONString(page.getRecords()));
        System.err.println(page.getTotal());

    }

}
