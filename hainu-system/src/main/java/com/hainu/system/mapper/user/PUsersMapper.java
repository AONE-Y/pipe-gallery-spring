package com.hainu.system.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.system.model.user.PUsers;
import com.hainu.system.model.user.PUsersExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PUsersMapper extends BaseMapper<PUsers> {

}