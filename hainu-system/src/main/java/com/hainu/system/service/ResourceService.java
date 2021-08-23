package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.dto.Router;
import com.hainu.system.entity.Resource;

import java.util.List;

public interface ResourceService extends IService<Resource> {
    /**
     * 查询用户权限资源
     * @param userId
     * @return
     */
    List<Router> queryUserResource(String userId);

    /**
     * 查询用户权限集合
     */
    List<Resource> queryUserPerm(String userId);
}
