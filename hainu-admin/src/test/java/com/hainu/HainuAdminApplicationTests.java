package com.hainu;


import cn.hutool.core.date.LocalDateTimeUtil;
import com.hainu.system.dao.DeviceListMapper;
import com.hainu.system.entity.DeviceLog;
import com.hainu.system.service.DeviceCurrentService;
import com.hainu.system.service.DeviceListService;
import com.hainu.system.service.DeviceLogService;
import com.hainu.system.service.LoginInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class HainuAdminApplicationTests {


    @Autowired
    LoginInfoService ls;

    @Autowired
    DeviceListService dls;

    @Autowired
    DeviceListMapper dlm;

    @Autowired
    DeviceCurrentService dcs;

    @Autowired
    DeviceLogService dlogs;




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

        // InfoController infoController = new InfoController();
        // Result<?> sysInfo = infoController.getSysInfo();
        // System.out.println(sysInfo.toString());


        // DeviceList dl = new DeviceList();
        // dl.setDeviceTopic("/gad/sda");
        // dl.setDeviceName("gad");
        //
        // // dls.save(dl);
        // // dlm.deleteData();
        //
        // DeviceController deviceController = new DeviceController();
        // deviceController.saveTopic("/dev/dae");

        //
        // DeviceCurrent deviceCurrent = new DeviceCurrent();
        // deviceCurrent.setDeviceName("123");
        // dcs.save(deviceCurrent);
        LocalDateTime now = LocalDateTime.now().minusMonths(1);
        DeviceLog deviceLog = new DeviceLog();
        LocalDateTimeUtil.format(now, "yyyy-MM-dd HH:MM:ss");
        deviceLog.setDeviceName("12375765");
        deviceLog.setCreateTime(LocalDateTime.now());
        dlogs.save(deviceLog);

    }


}
