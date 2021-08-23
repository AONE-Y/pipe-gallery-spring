package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_user")
public class User {
    private static final long  serialVersionUID = 1L;

    @TableId(value = "user_id",type = IdType.ID_WORKER_STR)
    private String userId;

    @TableField("user_account")
    private String userAccount;

    @TableField("user_name")
    private String userName;

    @TableField("user_nick_name")
    private String userNickName;

    @TableField("user_password")
    private String userPassword;

    @TableField("user_age")
    private Integer userAge;

    @TableField("user_sex")
    private String userSex;

    @TableField("user_mobile_phone")
    private String userMobilePhone;

    @TableField("user_mail")
    private String userMail;

    @TableField("user_desc")
    private String userDesc;

    @TableField("user_state")
    private Integer userState;

    @TableField("user_del_flag")
    private Integer userDelFlag;

    @TableField("user_createtime")
    private String userCreatetime;

    @TableField("user_updatetime")
    private String userUpdateTime;

    @TableField("user_img")
    private String userImg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserMobilePhone() {
        return userMobilePhone;
    }

    public void setUserMobilePhone(String userMobilePhone) {
        this.userMobilePhone = userMobilePhone;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    public Integer getUserDelFlag() {
        return userDelFlag;
    }

    public void setUserDelFlag(Integer userDelFlag) {
        this.userDelFlag = userDelFlag;
    }

    public String getUserCreatetime() {
        return userCreatetime;
    }

    public void setUserCreatetime(String userCreatetime) {
        this.userCreatetime = userCreatetime;
    }

    public String getUserUpdateTime() {
        return userUpdateTime;
    }

    public void setUserUpdateTime(String userUpdateTime) {
        this.userUpdateTime = userUpdateTime;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", userName='" + userName + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userAge=" + userAge +
                ", userSex='" + userSex + '\'' +
                ", userMobilePhone='" + userMobilePhone + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userDesc='" + userDesc + '\'' +
                ", userState=" + userState +
                ", userDelFlag=" + userDelFlag +
                ", userCreatetime='" + userCreatetime + '\'' +
                ", userUpdateTime='" + userUpdateTime + '\'' +
                ", userImg='" + userImg + '\'' +
                '}';
    }
}
