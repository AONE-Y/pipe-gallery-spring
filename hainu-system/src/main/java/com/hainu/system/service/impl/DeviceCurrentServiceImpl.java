package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.DeviceCurrentMapper;
import com.hainu.system.entity.DeviceCurrent;
import com.hainu.system.service.DeviceCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.service.impl
 * @Date：2021/9/15 20:35
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Service
public class DeviceCurrentServiceImpl extends ServiceImpl<DeviceCurrentMapper, DeviceCurrent> implements DeviceCurrentService {

    @Autowired
    DeviceCurrentMapper deviceCurrentMapper;

    @Override
    public void truncateData() {
        deviceCurrentMapper.truncateData();
    }
}

