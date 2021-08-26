package com.hainu.controller.systemManger;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.common.lang.Result;
import com.hainu.system.entity.Resource;
import com.hainu.system.service.ResourceService;
import com.hainu.common.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

@RestController
@RequestMapping("permission")
public class PermissionController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping("getList")
    @ResponseBody
    @SaCheckLogin
    public Result<?> getList(){
        List<Resource> list = resourceService.list();
        return new Result<>().success().put(list);
    }

    /**
     * 删除一个菜单
     */
    @RequestMapping("del")
    @ResponseBody
    @SaCheckLogin
    public Result<?> del(@RequestBody Map map){
        resourceService.removeById((Serializable) map.get("id"));
        return new Result<>().success();
    }

    /**
     * 添加菜单
     */
    @RequestMapping("add")
    @ResponseBody
    @SaCheckLogin
    public Result<?> add(@RequestBody Map map){
        Object res = map.get("resource");
        Resource resource = JSON.parseObject(JSON.toJSONString(res),Resource.class);
        String name = resource.getResourceName();
        QueryWrapper<Resource> wrapper = new QueryWrapper<>();
        wrapper.eq("resource_name",name);
        int count = resourceService.count(wrapper);
        if(count > 0){
            return new Result<>().error(500,"该菜单已存在！");
        }
        resource.setResourceDelFlag(0);
        resource.setResourceCreatetime(DateUtil.formatTime(new Date()));
        resource.setResourceUpdatetime(DateUtil.formatTime(new Date()));
        resourceService.save(resource);
        return new Result<>().success();
    }

    /**
     * 修改菜单
     */
    @RequestMapping("update")

    @ResponseBody
    @SaCheckLogin
    public Result<?> update(@RequestBody Map map){
        Object res = map.get("resource");
        Resource resource = JSON.parseObject(JSON.toJSONString(res),Resource.class);
        resource.setResourceUpdatetime(DateUtil.formatTime(new Date()));
        resourceService.updateById(resource);
        return new Result<>().success();
    }


    /**
     * 获取父级菜单下拉框
     */
    @RequestMapping("getCascader")
    @ResponseBody
    @SaCheckLogin
    public Result<?> getCascader(){
        List<Resource> list = resourceService.list();
        List<Map> cas = new ArrayList<>();
        Map mainPerm = new HashMap();
        mainPerm.put("label","主菜单");
        mainPerm.put("value","0");
        cas.add(mainPerm);
        for (Resource resource : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("label",resource.getResourceName());
            map.put("value",resource.getResourceId());
            cas.add(map);
            if(resource.getChildren() != null && resource.getChildren().size() > 0){
                List<Map> child = getCasChildren(resource.getChildren());
                map.put("children",child);
            }
        }
        return new Result<>().success().put(cas);
    }

    private List<Map> getCasChildren(List<Resource> resources){
        List<Map> cas = new ArrayList<>();
        for (Resource resource : resources) {
            Map<String,Object> map = new HashMap<>();
            map.put("label",resource.getResourceName());
            map.put("value",resource.getResourceId());
            cas.add(map);
            if(resource.getChildren() != null && resource.getChildren().size() > 0){
                List<Map> child = getCasChildren(resource.getChildren());
                map.put("children",child);
            }
        }
        return  cas;
    }
}
