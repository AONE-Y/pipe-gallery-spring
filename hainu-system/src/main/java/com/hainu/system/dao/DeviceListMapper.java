package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.DeviceList;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.dao
 * @Date：2021/9/9 16:01
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Mapper
public interface DeviceListMapper extends BaseMapper<DeviceList> {
    void truncateData();
}