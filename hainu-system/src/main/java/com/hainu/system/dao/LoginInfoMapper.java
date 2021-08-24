package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.LoginInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginInfoMapper extends BaseMapper<LoginInfo> {
}