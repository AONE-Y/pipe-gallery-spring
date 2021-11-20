package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.DeviceQuery;
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
public interface DeviceQueryMapper extends BaseMapper<DeviceQuery> {
        List<DeviceQuery> getDeviceQueryInfo(@Param("wsName") String wsName);
}