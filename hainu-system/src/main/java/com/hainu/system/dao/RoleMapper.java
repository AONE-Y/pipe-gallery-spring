package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;

@Mapper
@Resource
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取用户角色
     */
    Role queryRoleByUserId(@Param("userId") String userId);

}
