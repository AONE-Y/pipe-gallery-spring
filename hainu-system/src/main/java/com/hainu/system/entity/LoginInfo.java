package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
    * 系统访问记录
    */
@ApiModel(value="com-hainu-system-entity-LoginInfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "testshardingjdbc.sys_logininfor")
public class LoginInfo implements Serializable {
    /**
     * 访问ID
     */
    @TableId(value = "info_id", type = IdType.AUTO)
    @ApiModelProperty(value="访问ID")
    private Long infoId;

    /**
     * 用户账号
     */
    @TableField(value = "user_account")
    @ApiModelProperty(value="用户账号")
    private String userAccount;

    /**
     * 昵称
     */
    @TableField(value = "user_nick_name")
    @ApiModelProperty(value="昵称")
    private String userNickName;

    /**
     * 登录IP地址
     */
    @TableField(value = "ipaddr")
    @ApiModelProperty(value="登录IP地址")
    private String ipaddr;

    /**
     * 登录地点
     */
    @TableField(value = "login_location")
    @ApiModelProperty(value="登录地点")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @TableField(value = "browser")
    @ApiModelProperty(value="浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @TableField(value = "os")
    @ApiModelProperty(value="操作系统")
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="登录状态（0成功 1失败）")
    private String status;

    /**
     * 提示消息
     */
    @TableField(value = "msg")
    @ApiModelProperty(value="提示消息")
    private String msg;

    /**
     * 访问时间
     */
    @TableField(value = "login_time")
    @ApiModelProperty(value="访问时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime loginTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_INFO_ID = "info_id";

    public static final String COL_USER_ACCOUNT = "user_account";

    public static final String COL_USER_NICK_NAME = "user_nick_name";

    public static final String COL_IPADDR = "ipaddr";

    public static final String COL_LOGIN_LOCATION = "login_location";

    public static final String COL_BROWSER = "browser";

    public static final String COL_OS = "os";

    public static final String COL_STATUS = "status";

    public static final String COL_MSG = "msg";

    public static final String COL_LOGIN_TIME = "login_time";
}