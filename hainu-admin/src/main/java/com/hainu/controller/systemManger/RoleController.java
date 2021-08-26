package com.hainu.controller.systemManger;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.system.common.result.Result;
import com.hainu.system.entity.Role;
import com.hainu.system.entity.*;
import com.hainu.system.service.RoleResourceService;
import com.hainu.system.service.RoleService;
import com.hainu.system.service.UserRoleService;
import com.hainu.system.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private UserRoleService userRoleService;

    @RequestMapping("getList")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> getList() {
        List<Role> list = roleService.list();
        return new Result<>().success().put(list);
    }

    @RequestMapping("currentAuth")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> currentAuth(@RequestBody Map map) {
        String id = (String) map.get("id");
        QueryWrapper<RoleResource> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", id);
        List<RoleResource> list = roleResourceService.list(wrapper);
        return new Result<>().success().put(list);
    }

    @RequestMapping("setAuth")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> setAuth(@RequestBody Map map) {
        Map roleMap = (Map) map.get("auth");
        String roleId = (String) roleMap.get("roleId");
        List<String> permIds = (List<String>) roleMap.get("permIds");
        //删除当前为RoleId的权限
        QueryWrapper<RoleResource> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        roleResourceService.remove(wrapper);
        //循环插入新的权限
        for (String permId : permIds) {
            RoleResource roleResource = new RoleResource();
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(permId);
            //避免插入重复 插入要查询是否存在
            QueryWrapper<RoleResource> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("resource_id", permId).eq("role_id", roleId);
            int count = roleResourceService.count(wrapper1);
            if (count == 0) {
                roleResourceService.save(roleResource);
            }
        }
        return new Result<>().success();
    }


    @RequestMapping("add")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> add(@RequestBody Map map) {
        Object res = map.get("role");
        Role role = JSON.parseObject(JSON.toJSONString(res), Role.class);
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", role.getRoleName());
        int count = roleService.count(wrapper);
        if (count > 0) {
            return new Result<>().error(500, "该角色已存在！");
        }
        role.setRoleUpdatetime(DateUtil.formatTime(new Date()));
        role.setRoleCreatetime(DateUtil.formatTime(new Date()));
        role.setRoleState(1);
        roleService.save(role);
        //每一个角色创建出来都要给默认的欢迎页面
        RoleResource roleResource = new RoleResource();
        roleResource.setRoleId(role.getRoleId());
        roleResource.setResourceId("3");
        roleResourceService.save(roleResource);
        return new Result<>().success();
    }


    @RequestMapping("update")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> update(@RequestBody Map map) {
        Object res = map.get("role");
        Role role = JSON.parseObject(JSON.toJSONString(res), Role.class);
        role.setRoleUpdatetime(DateUtil.formatTime(new Date()));
        roleService.updateById(role);
        return new Result<>().success();
    }

    @RequestMapping("del")
    @ResponseBody
    @SaCheckLogin
    public Result<?> del(@RequestBody Map map) {
        String id = (String) map.get("id");
        roleService.removeById(id);
        //同时删除角色权限表的数据
        QueryWrapper<RoleResource> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", id);
        roleResourceService.remove(wrapper);
        return new Result<>().success();
    }

    @RequestMapping("getUserRole")
    @SaCheckLogin
    @ResponseBody
    public Result<?> getUserRole(String userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        UserRole userRole = userRoleService.getOne(wrapper);
        return new Result<>().success().put(userRole.getRoleId());
    }

}
