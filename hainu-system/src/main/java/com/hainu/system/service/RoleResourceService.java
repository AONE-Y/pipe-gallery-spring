package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.entity.Resource;
import com.hainu.system.entity.RoleResource;

import java.util.List;

public interface RoleResourceService extends IService<RoleResource> {
    /**
     * 根据角色查询菜单
     *
     * @param roleId
     *            角色主键
     * @param status
     *            状态(0：禁用；1：启用)
     * @return
     */
    List<Resource> queryResourceByRoleId(String roleId);

}
