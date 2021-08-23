package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_role_resource")
public class RoleResource {

    private static final long  serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    @TableId(value = "role_id")
    private String roleId;

    @TableId(value = "resource_id")
    private String resourceId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
