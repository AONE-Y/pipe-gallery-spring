package com.hainu;


import com.hainu.controller.device.DeviceController;
import com.hainu.system.dao.DeviceListMapper;
import com.hainu.system.entity.DeviceList;
import com.hainu.system.service.DeviceListService;
import com.hainu.system.service.LoginInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HainuAdminApplicationTests {


    @Autowired
    LoginInfoService ls;

    @Autowired
    DeviceListService dls;

    @Autowired
    DeviceListMapper dlm;




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


        DeviceList dl = new DeviceList();
        dl.setDeviceTopic("/gad/sda");
        dl.setDeviceName("gad");

        // dls.save(dl);
        // dlm.deleteData();

        DeviceController deviceController = new DeviceController();
        deviceController.saveTopic("/dev/dae");

    }


}
