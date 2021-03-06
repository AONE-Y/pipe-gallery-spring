package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.DeviceCurrent;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.dao
 * @Date：2021/10/18 11:59
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Mapper
public interface DeviceCurrentMapper extends BaseMapper<DeviceCurrent> {
    void truncateData();
}