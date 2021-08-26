package com.hainu.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hainu.common.dto.Router;
import com.hainu.system.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 查询用户权限资源
     * @param userId
     * @return
     */
    List<Router> queryUserResource(@Param("userId") String userId, @Param("parentId")String parentId);

    /**
     * 查询用户权限集合
     */
    List<Resource> queryUserPerm(@Param("userId") String userId);
}
