package com.hainu;


import com.hainu.common.lang.Result;
import com.hainu.controller.info.InfoController;
import com.hainu.system.service.LoginInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HainuAdminApplicationTests {


    @Autowired
    LoginInfoService ls;



    @Test
    void contextLoads() {
    }

    @Test
    void testList() {

        // List<OSFileStore> fileSystemInfo = SysInfoUtil.getFileSystemInfo();
        // for (OSFileStore osFileStore : fileSystemInfo) {
        //     System.out.println(osFileStore.getDescription());
        // }
        // System.out.println(ls.list());
        InfoController infoController = new InfoController();
        Result<?> sysInfo = infoController.getSysInfo();
        System.out.println(sysInfo.toString());
    }


}
