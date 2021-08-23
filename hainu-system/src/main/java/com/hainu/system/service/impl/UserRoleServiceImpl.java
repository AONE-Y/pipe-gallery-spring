package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.UserRoleMapper;
import com.hainu.system.entity.Role;
import com.hainu.system.entity.UserRole;
import com.hainu.system.service.RoleService;
import com.hainu.system.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private RoleService roleService;

    @Override
    public List<Role> queryRolesByUserId(String userId) {
        QueryWrapper<UserRole> ew = new QueryWrapper<>();
        ew.eq("user_id", userId);
        List<UserRole> userRoleList = this.list(ew);
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<String> roleIds = new ArrayList<String>();
            for (UserRole userRole : userRoleList) {
                roleIds.add(userRole.getRoleId());
            }
            QueryWrapper<Role> ewRole = new QueryWrapper<>();
            ewRole.in("role_id", roleIds);
            List<Role> roleList = roleService.list(ewRole);
            return roleList;
        } else {
            return null;
        }
    }
}
