package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.common.annotation.Cach;
import com.hainu.system.dao.LoginInfoMapper;
import com.hainu.system.entity.LoginInfo;
import com.hainu.system.service.LoginInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo> implements LoginInfoService {


    @Override
    public LoginInfo test() {
        return getById(438);
    }

    @Cach(key="test",expire = 3600)
    @Override
    public List<LoginInfo> list() {
        System.out.println("test");
        return super.list();
    }
}
