package com.hainu.system.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Router {
    //对应router.map.js名称
    private String router;
    //路径
    private String path;
    //图表
    private String icon;
    //是否隐藏1不隐藏0隐藏
    private Integer ifshow;
    private boolean invisible;
    //标题
    private String title;
    //权限集合
    private Map authority;
    //面包屑
    private String crumb;
    //父级
    private String parent;
    private String id;
    private String name;
    //权重
    private Integer level;
    private String auth;
    private Map page;
    private List<Router> Children;
}
