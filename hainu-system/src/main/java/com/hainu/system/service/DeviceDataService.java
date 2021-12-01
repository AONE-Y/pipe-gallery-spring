package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.entity.DeviceData;
import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.service
 * @Date：2021/11/21 10:38
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
public interface DeviceDataService extends IService<DeviceData> {
    List<DeviceData> getDeviceData(String wsName);

}

