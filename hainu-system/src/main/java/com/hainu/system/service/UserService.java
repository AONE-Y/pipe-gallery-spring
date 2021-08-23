package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.entity.User;

public interface UserService extends IService<User> {
    /**
     * 根据用户名查询用户
     *
     * @param userAccount 用户账号
     * @return 用户
     */
    User queryUserByAccount(String userAccount);
}
