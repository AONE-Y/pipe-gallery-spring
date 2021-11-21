package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.DeviceData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.dao
 * @Date：2021/11/21 11:50
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Mapper
public interface DeviceDataMapper extends BaseMapper<DeviceData> {
    List<DeviceData> getDeviceData();
}