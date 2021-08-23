package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.entity.Role;

public interface RoleService extends IService<Role> {
    /**
     * 根据用户信息获取用户的Role
     */
    Role queryUserRole(String userId);
}
