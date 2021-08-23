package com.hainu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hainu.system.entity.User;
import com.hainu.system.service.UserService;
import com.hainu.system.dao.UserMapper;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Cacheable(value = "users", key = "'account_'.concat(#userAccount)")
    public User queryUserByAccount(String userAccount) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.and(e -> e.eq("user_account", userAccount).or().eq("user_mail", userAccount).or().eq("user_mobile_phone",
                userAccount));
        return this.getOne(ew);
    }
}
