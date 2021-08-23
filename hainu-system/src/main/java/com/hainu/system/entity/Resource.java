package com.hainu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.ArrayList;
import java.util.List;

@TableName("sys_resource")
public class Resource {

    private static final long  serialVersionUID = 1L;

    @TableId(value = "resource_id",type = IdType.ID_WORKER_STR)
    private String resourceId;

    @TableField("resource_name")
    private String resourceName;

    @TableField("resource_parent_id")
    private String resourceParentId;

    @TableField("resource_type")
    private String resourceType;

    @TableField("resource_icon")
    private String resourceIcon;

    @TableField("resource_url")
    private String resourceUrl;

    @TableField("resource_level")
    private String resourceLevel;

    @TableField("resource_show")
    private Integer resourceShow;

    @TableField("resource_desc")
    private String resourceDesc;

    @TableField("resource_createtime")
    private String resourceCreatetime;

    @TableField("resource_updatetime")
    private String resourceUpdatetime;

    @TableField("resource_del_flag")
    private Integer resourceDelFlag;

    @TableField("resource_auth")
    private String resourceAuth;

    @TableField("resource_crumb")
    private String resourceCrumb;

    public String getResourceCrumb() {
        return resourceCrumb;
    }

    public void setResourceCrumb(String resourceCrumb) {
        this.resourceCrumb = resourceCrumb;
    }

    public String getResourceAuth() {
        return resourceAuth;
    }

    public void setResourceAuth(String resourceAuth) {
        this.resourceAuth = resourceAuth;
    }

    /**
     * 子菜单，必须初始化否则vue新增不展示树子菜单
     */
    @TableField(exist = false)
    private List<Resource> children = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Resource> getChildren() {
        return children;
    }

    public void setChildren(List<Resource> children) {
        this.children = children;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceParentId() {
        return resourceParentId;
    }

    public void setResourceParentId(String resourceParentId) {
        this.resourceParentId = resourceParentId;
    }


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(String resourceIcon) {
        this.resourceIcon = resourceIcon;
    }


    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(String resourceLevel) {
        this.resourceLevel = resourceLevel;
    }


    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public String getResourceCreatetime() {
        return resourceCreatetime;
    }

    public void setResourceCreatetime(String resourceCreatetime) {
        this.resourceCreatetime = resourceCreatetime;
    }

    public String getResourceUpdatetime() {
        return resourceUpdatetime;
    }

    public void setResourceUpdatetime(String resourceUpdatetime) {
        this.resourceUpdatetime = resourceUpdatetime;
    }

    public Integer getResourceShow() {
        return resourceShow;
    }

    public void setResourceShow(Integer resourceShow) {
        this.resourceShow = resourceShow;
    }

    public Integer getResourceDelFlag() {
        return resourceDelFlag;
    }

    public void setResourceDelFlag(Integer resourceDelFlag) {
        this.resourceDelFlag = resourceDelFlag;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", resourceParentId='" + resourceParentId + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", resourceIcon='" + resourceIcon + '\'' +
                ", resourceUrl='" + resourceUrl + '\'' +
                ", resourceLevel='" + resourceLevel + '\'' +
                ", resourceShow='" + resourceShow + '\'' +
                ", resourceDesc='" + resourceDesc + '\'' +
                ", resourceCreatetime='" + resourceCreatetime + '\'' +
                ", resourceUpdatetime='" + resourceUpdatetime + '\'' +
                ", resourceDelFlag='" + resourceDelFlag + '\'' +
                '}';
    }
}
