package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.entity.Role;
import com.hainu.system.entity.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    /**
     * queryRolesByUserId
     *
     * @Title: queryRolesByUserId
     * @Description: 查询用户的角色
     * @param userId
     * @return List<Role>
     */
    List<Role> queryRolesByUserId(String userId);

}
