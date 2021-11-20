package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.DeviceQueryMapper;
import com.hainu.system.entity.DeviceQuery;
import com.hainu.system.service.DeviceQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.service.impl
 * @Date：2021/11/20 20:47
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description: 
 * @Modified By: ANONE
 */
@Service
public class DeviceQueryServiceImpl extends ServiceImpl<DeviceQueryMapper, DeviceQuery> implements DeviceQueryService{
   @Autowired
   private DeviceQueryMapper deviceQueryMapper;

    @Override
    public List<DeviceQuery> getDeviceQueryInfo(String wsName){
        return deviceQueryMapper.getDeviceQueryInfo(wsName);
    }
}
