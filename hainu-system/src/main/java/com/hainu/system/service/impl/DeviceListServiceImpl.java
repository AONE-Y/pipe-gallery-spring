package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.DeviceListMapper;
import com.hainu.system.entity.DeviceList;
import com.hainu.system.service.DeviceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.service.impl
 * @Date：2021/9/4 21:03
 * @Author：yy188
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: yy188
 */
@Service
public class DeviceListServiceImpl extends ServiceImpl<DeviceListMapper, DeviceList> implements DeviceListService {
    @Autowired
    private DeviceListMapper deviceListMapper;

    @Override
    public void truncateData() {
        deviceListMapper.truncateData();
    }
}








