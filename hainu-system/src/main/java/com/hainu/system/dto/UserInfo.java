package com.hainu.system.dto;

import com.hainu.system.entity.User;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class UserInfo {
    private User userInfo;
    private String roleName;
    private Set<String> permission;
    private List<Map> routers;

}
