package com.hainu.system.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "com-hainu-system-model-user-PUsers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "pipe_gallery.p_users")
public class PUsers implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 用户昵称
     */
    @TableField(value = "`name`")
    @ApiModelProperty(value = "用户昵称")
    private String name;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "passwd")
    @ApiModelProperty(value = "密码")
    private String passwd;

    /**
     * 用户角色，0为管理员，1为授权用户
     */
    @TableField(value = "`role`")
    @ApiModelProperty(value = "用户角色，0为管理员，1为授权用户")
    private String role;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_USERNAME = "username";

    public static final String COL_PASSWD = "passwd";

    public static final String COL_ROLE = "role";
}