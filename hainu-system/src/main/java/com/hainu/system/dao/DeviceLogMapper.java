package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.DeviceLog;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.time.LocalDate;import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.system.dao
 * @Date：2021/9/11 16:03
 * @Author：ANONE
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: ANONE
 */
@Mapper
public interface DeviceLogMapper extends BaseMapper<DeviceLog> {
    List<DeviceLog> selectByAvg(@Param("minDate") LocalDate minDate, @Param("maxDate") LocalDate maxDate,
                                @Param("wsName") String wsName, @Param("node") String node);
}