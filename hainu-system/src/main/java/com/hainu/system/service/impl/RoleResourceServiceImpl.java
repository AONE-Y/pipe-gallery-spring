package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.RoleResourceMapper;
import com.hainu.system.entity.Resource;
import com.hainu.system.entity.RoleResource;
import com.hainu.system.service.ResourceService;
import com.hainu.system.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {

    @Autowired
    private ResourceService resourceService;


    @Override
    public List<Resource> queryResourceByRoleId(String roleId) {
        QueryWrapper<RoleResource> ew = new QueryWrapper<>();
        ew.eq("role_id", roleId);
        List<RoleResource> roleResourceList = this.list(ew);
        if (!CollectionUtils.isEmpty(roleResourceList)) {
            List<String> resourceIds = new ArrayList<String>();
            for (RoleResource roleResource : roleResourceList) {
                resourceIds.add(roleResource.getResourceId());
            }
            QueryWrapper<Resource> ewResource = new QueryWrapper<>();
            ewResource.in("id", resourceIds);
            List<Resource> resourceList = resourceService.list(ewResource);
            return resourceList;
        } else {
            return null;
        }
    }
}
