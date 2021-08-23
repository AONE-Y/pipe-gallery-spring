package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class Role {

    private static final long  serialVersionUID = 1L;

    @TableId(value = "role_id",type = IdType.ID_WORKER_STR)
    private String roleId;

    @TableField("role_name")
    private String roleName;

    @TableField("role_state")
    private Integer roleState;

    @TableField("role_createtime")
    private String roleCreatetime;

    @TableField("role_updatetime")
    private String roleUpdatetime;

    @TableField("role_desc")
    private String roleDesc;


}
