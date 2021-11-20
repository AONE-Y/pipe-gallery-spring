package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.DeviceCmdMapper;
import com.hainu.system.entity.DeviceCmd;
import com.hainu.system.service.DeviceCmdService;
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
public class DeviceCmdServiceImpl extends ServiceImpl<DeviceCmdMapper, DeviceCmd> implements DeviceCmdService{

    @Autowired
    private DeviceCmdMapper deviceCmdMapper;
    @Override
    public List<DeviceCmd> getDeviceCmdInfo(String wsName){
        return deviceCmdMapper.getDeviceCmdInfo(wsName);
    }
}
