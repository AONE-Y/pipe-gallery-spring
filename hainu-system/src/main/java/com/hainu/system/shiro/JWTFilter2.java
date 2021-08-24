package com.hainu.system.shiro;


import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.auth0.jwt.impl.JWTParser;
import com.hainu.common.lang.Result;
import com.hainu.system.config.SpringContextBean;
import com.hainu.system.entity.User;
import com.hainu.system.service.UserService;
import com.hainu.system.util.JwtComponent;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter2 extends AuthenticatingFilter {

    @Autowired
    private JwtComponent jwtComponent;
    @Autowired
    private UserService userService;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)) {
            return null;
        }

        return new JWTToken(jwt);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURI();
        if(url.equals("/auth/login")){
            return true;
        }
        if(StringUtils.isEmpty(jwt)) {
            return true;
        } else {


            // 校验jwt
            JWT jwtInfo = JWTUtil.parseToken(jwt);
            if(jwtInfo == null || jwtInfo.validate(30)) {
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }


            // 执行登录
            return executeLogin(servletRequest, servletResponse);
        }
    }
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null) {
            JWTToken token = new JWTToken(authorization);
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            getSubject(request, response).login(token);
            // 如果没有抛出异常则代表登入成功，返回true
            setUserBean(request, response, token);
            return true;
        }
        return false;


    }
    private void setUserBean(ServletRequest request, ServletResponse response, JWTToken token) {
        if (this.userService == null) {
            this.userService = SpringContextBean.getBean(UserService.class);
        }
        String userAccount = jwtComponent.getUserAccount(String.valueOf(token.getCredentials()));
        User userBean = userService.queryUserByAccount(userAccount);
        request.setAttribute("currentUser", userBean);
    }
}
