package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.DeviceLogMapper;
import com.hainu.system.entity.DeviceLog;
import com.hainu.system.service.DeviceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.service.impl
 * @Date：2021/9/8 19:32
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Service
public class DeviceLogServiceImpl extends ServiceImpl<DeviceLogMapper, DeviceLog> implements DeviceLogService {

    @Autowired
    private DeviceLogMapper deviceLogMapper;



    @Override
    public List<DeviceLog> selectByAvg(LocalDate minDate, LocalDate maxDate,String deviceName) {
        return deviceLogMapper.selectByAvg(minDate, maxDate,deviceName);
    }
}


