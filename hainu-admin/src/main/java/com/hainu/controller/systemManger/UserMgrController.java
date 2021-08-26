package com.hainu.controller.systemManger;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.system.common.result.Result;
import com.hainu.system.entity.User;
import com.hainu.system.entity.UserRole;
import com.hainu.system.service.UserRoleService;
import com.hainu.system.service.UserService;
import com.hainu.system.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("userMgr")
public class UserMgrController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @RequestMapping("getList")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> getList() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_del_flag", 1);
        List<User> list = userService.list(wrapper);
        return new Result<>().success().put(list);
    }

    @RequestMapping("del")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> getList(@RequestBody Map map) {
        Object res = map.get("user");
        User user = JSON.parseObject(JSON.toJSONString(res), User.class);
        user.setUserDelFlag(0);
        userService.updateById(user);
        return new Result<>().success();
    }

    @RequestMapping("update")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> update(@RequestBody Map map) {
        Object res = map.get("user");
        User user = JSON.parseObject(JSON.toJSONString(res), User.class);
        String passwd = SecureUtil.md5(user.getUserPassword());
        user.setUserPassword(passwd);
        user.setUserUpdateTime(DateUtil.formatTime(new Date()));
        userService.updateById(user);
        return new Result<>().success();
    }

    @RequestMapping("add")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> add(@RequestBody Map map) {
        Object res = map.get("user");
        User user = JSON.parseObject(JSON.toJSONString(res), User.class);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", user.getUserAccount());
        int count = userService.count(wrapper);
        if (count > 0) {
            return new Result<>().error(500, "该用户已存在！");
        }
        String passwd = SecureUtil.md5(user.getUserPassword());
        user.setUserPassword(passwd);
        user.setUserDelFlag(1);
        user.setUserState(1);
        user.setUserCreatetime(DateUtil.formatTime(new Date()));
        user.setUserUpdateTime(DateUtil.formatTime(new Date()));
        userService.save(user);
        return new Result<>().success();
    }

    @RequestMapping("setRole")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> setRole(@RequestBody Map map) {
        Object res = map.get("userRole");
        UserRole userRole = JSON.parseObject(JSON.toJSONString(res), UserRole.class);
        //查询是否存在 存在更新 否则插入
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userRole.getUserId());
        UserRole userRole1 = userRoleService.getOne(wrapper);
        if (userRole1 != null) {
            UserRole userRole2 = new UserRole();
            userRole2.setId(userRole1.getId());
            userRole2.setUserId(userRole.getUserId());
            userRole2.setRoleId(userRole.getRoleId());
            userRoleService.updateById(userRole2);
        } else {
            userRoleService.save(userRole);
        }
        return new Result<>().success();
    }

    @RequestMapping("setUserPerson")
    @SaCheckLogin
    @ResponseBody
    @CrossOrigin
    public Result<?> setUserPerson(@RequestBody Map map) {
        Object res = map.get("user");
        User user = JSON.parseObject(JSON.toJSONString(res), User.class);
        user.setUserUpdateTime(DateUtil.formatTime(new Date()));
        userService.updateById(user);
        return new Result().success().put(user);
    }

    @RequestMapping("setAvatar")
    @ResponseBody
    @CrossOrigin
    @SaCheckLogin
    public Result<?> setAvatar(MultipartFile file) {
        Object loginId = StpUtil.getLoginId();
        User user = userService.getOne(new QueryWrapper<User>().eq("user_account", loginId));
        byte[] byt = null;
        try {
            byt = new byte[file.getInputStream().available()];
            file.getInputStream().read(byt);
            String res = Base64.getEncoder().encodeToString(byt);

            user.setUserImg("data:image/jpeg;base64," + res);
            userService.updateById(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result().success().put(user);
    }

    @RequestMapping("setUserPass")
    @ResponseBody
    @CrossOrigin
    @SaCheckLogin
    public Result<?> setUserPass(@RequestBody Map map) {
        Object loginId = StpUtil.getLoginId();
        User user = userService.getOne(new QueryWrapper<User>().eq("user_account", loginId));
        try {
            String oldPassMd5 = SecureUtil.md5((String) map.get("oldPass"));
            String newPassMd5 = SecureUtil.md5((String) map.get("newPass"));
            if (!oldPassMd5.equals(user.getUserPassword())) {
                return new Result<>().error(403, "旧密码输入错误");
            } else if (oldPassMd5.equals(newPassMd5)) {
                return new Result<>().error(406, "新密码不能与旧密码相同");
            } else {
                user.setUserPassword(newPassMd5);
                userService.updateById(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<>().success().put(user);
    }
}
