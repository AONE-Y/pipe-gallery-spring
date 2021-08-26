package com.hainu.controller.systemManger;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.common.lang.Result;
import com.hainu.common.dto.DateRange;
import com.hainu.system.entity.LoginInfo;
import com.hainu.system.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/info")
public class LoginInfoController {

    @Autowired
    LoginInfoService logininfoService;



    @CrossOrigin
    @SaCheckLogin
    @PostMapping("logInfo")
    public Result< ? > logInfo(@RequestBody(required = false)  DateRange date) {
        QueryWrapper<LoginInfo> queryWrapper =null;
        if (date != null) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.between("login_time",date.getBeginDate(),date.getEndDate());
        }
        return new Result<>().success().put(logininfoService.list(queryWrapper));
    }

}
