package com.hainu.system.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.service.DeviceCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.scheduled
 * @Date：2021/10/21 11:42
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    //3.添加定时任务
    // @Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    @Autowired
    private DeviceCurrentService deviceCurrentService;

    @Scheduled(fixedRate=10000)
    private void configureTasks() {
        QueryWrapper<DeviceCurrent> wrapper = new QueryWrapper<>();
        wrapper.le(DeviceCurrent.COL_UPDATE_TIME,LocalDateTime.now().plusSeconds(-60));
        DeviceCurrent deviceCurrent = DeviceCurrent.builder().status(0)
                .deviceTemp(0.0).deviceHumi(0.0).deviceGas(0.0)
                .deviceLlv(0.0).deviceO2(0.0).deviceSmoke(0.0)
                .deviceFan(0).deviceInfra(0).deviceLighting(0)
                .deviceManhole(0).deviceWaterpump(0).build();
        // DeviceCurrent deviceCurrent = DeviceCurrent.builder().status(0).build();
        deviceCurrentService.update(deviceCurrent,wrapper);
    }
}
