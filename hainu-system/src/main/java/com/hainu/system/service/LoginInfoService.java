package com.hainu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hainu.system.config.MybatisRedisCache;
import com.hainu.system.entity.LoginInfo;
import org.apache.ibatis.annotations.CacheNamespace;

public interface LoginInfoService extends IService<LoginInfo>{


}
