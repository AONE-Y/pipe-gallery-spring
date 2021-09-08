package com.hainu;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hainu.system.dao.DeviceListMapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.service.DeviceCurrentService;
import com.hainu.system.service.DeviceListService;
import com.hainu.system.service.DeviceLogService;
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
        DeviceCurrent deviceCurrent = new DeviceCurrent();
        String name="12345";
        deviceCurrent.setDeviceName(name);
        deviceCurrent.setDeviceHumi(1);

        UpdateWrapper<DeviceCurrent> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("device_name", name);
        if(!dcs.update(deviceCurrent,updateWrapper)){
            dcs.save(deviceCurrent);
        }

        // LocalDateTime now = LocalDateTime.now().minusMonths(1);
        // DeviceLog deviceLog = new DeviceLog();
        // LocalDateTimeUtil.format(now, "yyyy-MM-dd HH:MM:ss");
        // deviceLog.setDeviceName("12375765");
        // deviceLog.setCreateTime(LocalDateTime.now());
        // dlogs.save(deviceLog);

    }


}
