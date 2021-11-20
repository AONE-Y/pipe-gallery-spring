package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.entity.DeviceCmd;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.service
 * @Date：2021/11/20 20:47
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description: 
 * @Modified By: ANONE
 */
public interface DeviceCmdService extends IService<DeviceCmd>{
    public List<DeviceCmd> getDeviceCmdInfo( String wsName);

}
