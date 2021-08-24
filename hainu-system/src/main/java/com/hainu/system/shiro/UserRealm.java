package com.hainu.system.shiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hainu.system.config.SpringContextBean;
import com.hainu.system.entity.Resource;
import com.hainu.system.entity.Role;
import com.hainu.system.entity.User;
import com.hainu.system.service.*;
import com.hainu.system.util.JwtComponent;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: MyRealm
 * @Description: TODO
 * @author ANONE
 * @date 2021年8月22日 下午4:00:59
 */
@Component
public class UserRealm extends AuthorizingRealm {
    
    @Autowired
    private JwtComponent jwtComponent;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public boolean supports(AuthenticationToken token) {
        /**
         * 表示此Realm只支持JWTToken类型
         */
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("-----------------------权限认证---------------------");
        JSONObject userObj = JSON.parseObject(principals.toString());
        String userId = userObj.getString("id");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //查询用户角色并放入shiro
        ArrayList<String> roles = new ArrayList<>();
        Role currentUserRole = roleService.queryUserRole(userId);
        // 查询权限
        ArrayList<String> permissions = new ArrayList<>();
        List<Resource> currentUserResource = resourceService.queryUserPerm(userId);
        roles.add(currentUserRole.getRoleName());
        Set<String> allPerm = new HashSet<>();
        for (Resource resource : currentUserResource) {
            allPerm.add(resource.getResourceAuth());
        }
        Set<String> rolesSet = new HashSet<>(roles);
        simpleAuthorizationInfo.addRoles(rolesSet);
        simpleAuthorizationInfo.addStringPermissions(allPerm);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws UnauthorizedException {
        if (null == userService) {
            this.userService = SpringContextBean.getBean(UserService.class);
        }
        String token = (String) auth.getCredentials();
        // 解密token获得username，用于和数据库进行对比
        String userAccount = jwtComponent.getUserAccount(token);
        if (userAccount == null) {
            throw new UnauthorizedException("token invalid");
        }
        User userBean = userService.queryUserByAccount(userAccount);
        if (userBean == null) {
            throw new UnauthorizedException("User didn't existed!");
        }
        if (!jwtComponent.verify(token, userAccount, userBean.getUserPassword())) {
            throw new UnauthorizedException("Username or password error");
        }
        String userString = JSONObject.toJSONString(userBean);
        return new SimpleAuthenticationInfo(userString, token, this.getName());
    }
}
