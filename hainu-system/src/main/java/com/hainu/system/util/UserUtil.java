package com.hainu.system.util;

import com.hainu.system.dto.Router;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserUtil {
    /**
     * 递归取出所有权限
     * @param routers
     * @return
     */
    public static Set getAllPerm(List<Router> routers){
        Set<String> permSet = new HashSet<>();
        for (Router router : routers) {
            permSet.add(router.getAuth());
            if(router.getChildren() != null && router.getChildren().size() > 0){
                getAllPerm(router.getChildren());
            }
        }
        return permSet;
    }
}
