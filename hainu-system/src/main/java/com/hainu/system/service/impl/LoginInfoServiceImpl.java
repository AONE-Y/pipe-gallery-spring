package com.hainu.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.dao.LoginInfoMapper;
import com.hainu.system.entity.LoginInfo;
import com.hainu.system.service.LoginInfoService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo> implements LoginInfoService {


}
