package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.DeviceDataMapper;
import com.hainu.system.entity.DeviceData;
import com.hainu.system.service.DeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.service.impl
 * @Date：2021/11/21 10:38
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Service
public class DeviceDataServiceImpl extends ServiceImpl<DeviceDataMapper, DeviceData> implements DeviceDataService {
    @Autowired
    private DeviceDataMapper deviceDataMapper;

    @Override
    public List<DeviceData> getDeviceData(String wsName) {
        return deviceDataMapper.getDeviceData(wsName);
    }
}

