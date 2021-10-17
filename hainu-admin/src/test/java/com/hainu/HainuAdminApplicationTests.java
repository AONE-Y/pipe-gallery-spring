package com.hainu;


import com.hainu.system.dao.DeviceListMapper;
import com.hainu.system.dao.DeviceLogMapper;
import com.hainu.system.entity.DeviceList;
import com.hainu.system.entity.DeviceLog;
import com.hainu.system.entity.NodeSensor;
import com.hainu.system.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

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

    @Autowired
    DeviceLogMapper dlogm;

    @Autowired
    NodeSensorService nss;




    @Test
    void contextLoads() {

        List<NodeSensor> list1 = nss.list();



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
        // dl.setDeviceTopic("/gad/sda");
        // dl.setDeviceName("gad");

        dls.save(dl);
        // // dlm.deleteData();
        //
        // DeviceController deviceController = new DeviceController();
        // deviceController.saveTopic("/dev/dae");

        //
        // DeviceCurrent deviceCurrent = new DeviceCurrent();
        // String name="12345";
        // deviceCurrent.setDeviceName(name);
        // deviceCurrent.setDeviceHumi(1);
        //
        // UpdateWrapper<DeviceCurrent> updateWrapper = new UpdateWrapper<>();
        // updateWrapper.eq("device_name", name);
        // if(!dcs.update(deviceCurrent,updateWrapper)){
        //     dcs.save(deviceCurrent);
        // }

        // LocalDateTime now = LocalDateTime.now().minusMonths(1);
        // DeviceLog deviceLog = new DeviceLog();
        // LocalDateTimeUtil.format(now, "yyyy-MM-dd HH:MM:ss");
        // deviceLog.setDeviceName("12375765");
        // deviceLog.setCreateTime(LocalDateTime.now());
        // dlogs.save(deviceLog);

    }

    @Test
    void testSql(){
        LocalDate date = LocalDate.now();
        LocalDate firstday = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        List<DeviceLog> deviceLogs = dlogm.selectByAvg(firstday, lastDay,null,null);
        deviceLogs.forEach(System.out::println);
    }


}
