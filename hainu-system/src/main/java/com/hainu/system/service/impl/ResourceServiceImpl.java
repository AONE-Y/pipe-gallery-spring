package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.ResourceMapper;
import com.hainu.common.dto.Router;
import com.hainu.system.entity.Resource;
import com.hainu.system.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;


    @Override
    public List<Router> queryUserResource(String userId) {
        //查询出父级菜单
        List<Router> parentPerm = resourceMapper.queryUserResource(userId,"0");
        if(parentPerm != null && parentPerm.size() > 0){
            parentPerm.forEach(p -> {findAllChild(p,userId);});
        }
        return parentPerm;
    }

    @Override
    public List<Resource> queryUserPerm(String userId) {
        return resourceMapper.queryUserPerm(userId);
    }

    public void  findAllChild(Router router,String userId){
        List<Router> resources = resourceMapper.queryUserResource(userId,router.getId());
        router.setChildren(resources);
        if (resources != null && resources.size() > 0) {
            resources.forEach(c -> {findAllChild(c,userId);});
        }
    }

    @Override
    public List<Resource> list(){
        QueryWrapper<Resource> wrapper = new QueryWrapper<>();
        wrapper.eq("resource_parent_id","0").eq("resource_del_flag",0).orderByDesc("resource_level");
        List<Resource> list = this.list(wrapper);
        if(list != null && list.size() > 0){
            list.forEach(this::findAllChild);
        }
        return list;
    }

    public void findAllChild( Resource resource ) {
        QueryWrapper<Resource> wrapper = new QueryWrapper<>();
        wrapper.eq("resource_parent_id", resource.getResourceId()).eq("resource_del_flag",0).orderByDesc("resource_level");
        List<Resource> resources = this.list(wrapper);
        resource.setChildren(resources);
        if (resources != null && resources.size() > 0) {
            resources.forEach(this::findAllChild);
        }
    }
}
