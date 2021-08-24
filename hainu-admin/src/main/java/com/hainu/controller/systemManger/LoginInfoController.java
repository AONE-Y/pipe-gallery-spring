package com.hainu.controller.systemManger;

import com.hainu.system.common.result.Result;
import com.hainu.system.entity.LoginInfo;
import com.hainu.system.service.LoginInfoService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
public class LoginInfoController {

    @Autowired
    LoginInfoService logininfoService;

    @GetMapping("/login")
    public Result<?> login() {
        List<LoginInfo> list = logininfoService.list();
        return new Result<>().success().put(list);
    }


}
