package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.RoleMapper;
import com.hainu.system.entity.Role;
import com.hainu.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role queryUserRole(String userId) {
        return roleMapper.queryRoleByUserId(userId);
    }
}
