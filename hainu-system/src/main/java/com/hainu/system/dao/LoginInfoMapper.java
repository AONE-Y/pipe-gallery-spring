package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.LoginInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface LoginInfoMapper extends BaseMapper<LoginInfo> {
}