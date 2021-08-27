package com.hainu;


import com.hainu.common.util.utils.SysInfoUtil;
import com.hainu.controller.analysis.SysInfoController;
import com.hainu.system.service.LoginInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import oshi.software.os.OSFileStore;

import java.util.List;

@SpringBootTest
class HainuAdminApplicationTests {


    @Autowired
    LoginInfoService ls;

    @Autowired
    SysInfoController sc;

    @Test
    void contextLoads() {
    }

    @Test
    void testList() {

        List<OSFileStore> fileSystemInfo = SysInfoUtil.getFileSystemInfo();
        for (OSFileStore osFileStore : fileSystemInfo) {
            System.out.println(osFileStore.getDescription());
        }
    }

}
