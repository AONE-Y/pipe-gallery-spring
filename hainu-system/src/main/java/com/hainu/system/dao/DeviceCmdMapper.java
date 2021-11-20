package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.DeviceCmd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.dao
 * @Date：2021/11/20 20:47
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description: 
 * @Modified By: ANONE
 */
@Mapper
public interface DeviceCmdMapper extends BaseMapper<DeviceCmd> {
    List<DeviceCmd> getDeviceCmdInfo(@Param("wsName") String wsName);
}