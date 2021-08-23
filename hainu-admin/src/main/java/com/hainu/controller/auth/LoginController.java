package com.hainu.controller.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.system.common.annotation.CurrentUser;
import com.hainu.system.common.result.ResponseConstant;
import com.hainu.system.common.result.Result;
import com.hainu.system.dto.Router;
import com.hainu.system.dto.UserInfo;
import com.hainu.system.entity.Resource;
import com.hainu.system.entity.Role;
import com.hainu.system.entity.User;
import com.hainu.system.service.ResourceService;
import com.hainu.system.service.RoleService;
import com.hainu.system.service.UserService;
import com.hainu.system.util.Constant;
import com.hainu.system.util.JwtComponent;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("/auth")
@RestController
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtComponent jwtComponent;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleService roleService;


    @RequestMapping("/login")
    @CrossOrigin
    public Result<Object> login(@RequestBody User loginUser, HttpServletRequest request){
        String userAccount = loginUser.getUserAccount();
        String userPassword = loginUser.getUserPassword();
        if(ObjectUtils.isEmpty(userAccount) || ObjectUtils.isEmpty(userPassword)){
            return new Result<Object>().error("参数错误");
        }
        //查询用户是否存在
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("user_account",userAccount);
        User user = userService.getOne(ew);
        if(!ObjectUtils.isEmpty(user)){
            if(!user.getUserPassword().equals(userPassword)){
                return new Result<Object>().error("账号或密码错误");
            }
            if(user.getUserState() == 0){
                return new Result<Object>().error("账号已禁用");
            }
            if(user.getUserDelFlag() == 0){
                return new Result<Object>().error("账号已失效");
            }
        }else{
            return new Result<Object>().error("用户不存在");
        }
        String token = jwtComponent.sign(user.getUserAccount(),user.getUserPassword(), Constant.ExpTimeType.WEB);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("token",token);
        return new Result<Object>().success().put(map);
    }

    /**
     * 获取个人信息
     * @return
     */
    @CrossOrigin
    @RequestMapping("/user/info")
    @RequiresAuthentication
    public Result<UserInfo> userInfo(HttpServletRequest request,@CurrentUser String token){
        UserInfo userInfo = new UserInfo();
        //放入用户角色
        String userAccount = jwtComponent.getUserAccount(token);
        User currentUser = userService.getOne(new QueryWrapper<User>().eq("user_account", userAccount));
        userInfo.setUserInfo(currentUser);
        Role userRole = roleService.queryUserRole(currentUser.getUserId());
        userInfo.setRoleName(userRole.getRoleName());
        //递归查询出用户菜单并执行操作
        List<Router> userPerm = resourceService.queryUserResource(currentUser.getUserId());
        for (Router router : userPerm) {
            Set<String> set = null;
            Map<String,Object> pageMap = new HashMap<>();
            pageMap.put("title",router.getRouter());
            pageMap.put("breadcrumb",new HashSet<String>(Arrays.asList(router.getCrumb().split(","))));
            router.setPage(pageMap);
            //权限处理 目前只放permission
            Map<String,String> authMap = new HashMap<>();
            authMap.put("permission",router.getAuth());
            router.setAuthority(authMap);
            //是否显示处理
            router.setInvisible(router.getIfshow() == 1 ? false : true);
            if(router.getChildren() != null && router.getChildren().size() > 0){
                parseRouter(router.getChildren());
            }
        }
        //菜单包括用户菜单和用户菜单权限
        List<Resource> perms = resourceService.queryUserPerm(currentUser.getUserId());
        Set<String> userAuth = new HashSet<>();
        for (Resource perm : perms) {
            userAuth.add(perm.getResourceAuth());
        }
        //将用户菜单转化为前端需要的格式
        List<Map> userTreePerm = new ArrayList<>();
        Map treeMap = new HashMap();
        treeMap.put("router","root");
        treeMap.put("children",userPerm);
        userTreePerm.add(treeMap);
        userInfo.setPermission(userAuth);
        userInfo.setRouters(userTreePerm);
        return new Result<UserInfo>().success().put(userInfo);
    }

    /**
     * 菜单递归操作
     * @return
     */
    private static void parseRouter(List<Router> routers){
        for (Router router : routers) {
            //meta中的page属性
            Map<String,Object> pageMap = new HashMap<>();
            pageMap.put("title",router.getRouter());
            pageMap.put("breadcrumb",new HashSet<String>(Arrays.asList(router.getCrumb().split(","))));
            router.setPage(pageMap);
            //权限处理 目前只放permission
            Map<String,String> authMap = new HashMap<>();
            authMap.put("permission",router.getAuth());
            router.setAuthority(authMap);
            //是否显示处理
            router.setInvisible(router.getIfshow() == 1 ? false : true);
            if(router.getChildren() != null && router.getChildren().size() > 0){
                parseRouter(router.getChildren());
            }
        }
    }
    

    @RequestMapping("401")
    @RequiresAuthentication
    public Result<?> unauthorized() {
        return new Result<>().error(ResponseConstant.USER_NO_PERMITION);
    }


    @PostMapping("/logout")
    public Result<?> logOut(HttpServletRequest request) throws Exception {
        return new Result<>().success();
    }


}
