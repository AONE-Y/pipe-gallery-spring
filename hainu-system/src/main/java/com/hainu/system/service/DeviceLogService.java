package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.entity.DeviceLog;
import java.util.List;

import java.time.LocalDate;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.service
 * @Date：2021/9/8 19:32
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public interface DeviceLogService extends IService<DeviceLog> {

    List<DeviceLog> selectByAvg(LocalDate minDate, LocalDate maxDate, String wsName, String node);

}






