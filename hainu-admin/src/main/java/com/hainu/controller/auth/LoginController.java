package com.hainu.controller.auth;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hainu.system.common.result.ResponseConstant;
import com.hainu.system.common.result.Result;
import com.hainu.system.dto.Router;
import com.hainu.system.dto.UserInfo;
import com.hainu.system.entity.Resource;
import com.hainu.system.entity.Role;
import com.hainu.system.entity.LoginInfo;
import com.hainu.system.entity.User;
import com.hainu.system.service.ResourceService;
import com.hainu.system.service.RoleService;
import com.hainu.system.service.LoginInfoService;
import com.hainu.system.service.UserService;
import com.hainu.system.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RequestMapping("/auth")
@RestController
public class LoginController {


    @Autowired
    private UserService userService;
    @Autowired
    private LoginInfoService logininfoService;

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleService roleService;


    @RequestMapping("/login")
    @CrossOrigin
    public Result<?> login(@RequestBody User loginUser, HttpServletRequest request) {
        LoginInfo logininfo = new LoginInfo();
        Map<String, String[]> loginFlag = new HashMap<>();


        String ip = IpUtil.getIpAddr(request);
        UserAgent ua = UserAgentUtil.parse(request.getHeader("User-Agent"));
        String userAccount = loginUser.getUserAccount();
        String userPassword = loginUser.getUserPassword();

        if (ObjectUtils.isEmpty(userAccount) || ObjectUtils.isEmpty(userPassword)) {
            return new Result<String>().error("参数错误");
        }
        //查询用户是否存在
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("user_account", userAccount);
        User user = userService.getOne(ew);
        //若value值未修改，则登入成功
        loginFlag.put("Flag", new String[]{"0", "登录成功"});
        if (!ObjectUtils.isEmpty(user)) {
            logininfo.setUserNickName(user.getUserNickName());
            if (!user.getUserPassword().equals(SecureUtil.md5(userPassword))) {
                loginFlag.put("Flag", new String[]{"4", "账号或密码错误"});
            }
            if (user.getUserState() == 0) {
                loginFlag.put("Flag", new String[]{"3", "账号已禁用"});

            }
            if (user.getUserDelFlag() == 0) {
                loginFlag.put("Flag", new String[]{"2", "账号已失效"});
            }

        } else {
            loginFlag.put("Flag", new String[]{"1", "用户不存在"});
        }

        //登录日志记录
        logininfo.setIpaddr(ip);
        logininfo.setLoginLocation(IpUtil.internalIp(ip) ? "内网" : "外网");
        logininfo.setBrowser(ua.getBrowser().toString());
        logininfo.setOs(ua.getOs().toString());
        logininfo.setUserAccount(userAccount);
        logininfo.setLoginTime(LocalDateTime.now());
        logininfo.setStatus(loginFlag.get("Flag")[0]);
        logininfo.setMsg(loginFlag.get("Flag")[1]);
        logininfoService.save(logininfo);

        if (loginFlag.get("Flag")[0].equals("0")) {

            StpUtil.login(loginUser.getUserAccount());
            return new Result<>().success().put(StpUtil.getTokenInfo());
        }

        return new Result<String>().error(loginFlag.get("Flag")[1]);


    }

    @RequestMapping("/user/info")
    @SaCheckLogin
    public Result<?> userInfo(HttpServletRequest request) {


        Object loginId = StpUtil.getLoginId();
        User currentUser = userService.getOne(new QueryWrapper<User>().eq("user_account", loginId));

        UserInfo userInfo = new UserInfo();
        //放入用户角色
        userInfo.setUserInfo(currentUser);
        Role userRole = roleService.queryUserRole(currentUser.getUserId());
        userInfo.setRoleName(userRole.getRoleName());
        //递归查询出用户菜单并执行操作
        List<Router> userPerm = resourceService.queryUserResource(currentUser.getUserId());
        for (Router router : userPerm) {
            Set<String> set = null;
            Map<String, Object> pageMap = new HashMap<>();
            pageMap.put("title", router.getRouter());
            pageMap.put("breadcrumb", new HashSet<String>(Arrays.asList(router.getCrumb().split(","))));
            router.setPage(pageMap);
            //权限处理 目前只放permission
            Map<String, String> authMap = new HashMap<>();
            authMap.put("permission", router.getAuth());
            router.setAuthority(authMap);
            //是否显示处理
            router.setInvisible(router.getIfshow() == 1 ? false : true);
            if (router.getChildren() != null && router.getChildren().size() > 0) {
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
        treeMap.put("router", "root");
        treeMap.put("children", userPerm);
        userTreePerm.add(treeMap);
        userInfo.setPermission(userAuth);
        userInfo.setRouters(userTreePerm);
        return new Result<UserInfo>().success().put(userInfo);
    }

    /**
     * 菜单递归操作
     *
     * @return
     */
    private static void parseRouter(List<Router> routers) {
        for (Router router : routers) {
            //meta中的page属性
            Map<String, Object> pageMap = new HashMap<>();
            pageMap.put("title", router.getRouter());
            pageMap.put("breadcrumb", new HashSet<String>(Arrays.asList(router.getCrumb().split(","))));
            router.setPage(pageMap);
            //权限处理 目前只放permission
            Map<String, String> authMap = new HashMap<>();
            authMap.put("permission", router.getAuth());
            router.setAuthority(authMap);
            //是否显示处理
            router.setInvisible(router.getIfshow() == 1 ? false : true);
            if (router.getChildren() != null && router.getChildren().size() > 0) {
                parseRouter(router.getChildren());
            }
        }
    }


    @RequestMapping("401")
    @SaCheckLogin
    public Result<?> unauthorized() {
        return new Result<>().error(ResponseConstant.USER_NO_PERMITION);
    }


    @PostMapping("/logout")
    public Result<?> logOut(HttpServletRequest request) throws Exception {
        StpUtil.logout();
        return new Result<>().success();
    }


}
