package com.hainu.system.config;

import com.hainu.system.service.ResourceService;
import com.hainu.system.shiro.JWTFilter;
import com.hainu.system.shiro.JWTFilter2;
import com.hainu.system.shiro.UserRealm;
import com.hainu.system.util.Constant;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ANONE
 * @ClassName: ShiroConfig
 * @Description:
 * @date 2021年8月22日 下午4:01:20
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private ResourceService resourceService;

    /**
     * 默认premission字符串
     */
    public static final String PERMISSION_STRING = "perms[\"{0}\"]";


    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

    //添加redis作为缓存
    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // inject redisSessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(UserRealm userRealm,
                                                     SessionManager sessionManager,
                                                     RedisCacheManager redisCacheManager) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(userRealm);

        //inject sessionManager
        securityManager.setSessionManager(sessionManager);

        // inject redisCacheManager
        securityManager.setCacheManager(redisCacheManager);
        return securityManager;
    }

    //    @Bean("securityManager")
//    public DefaultWebSecurityManager getManager(SessionManager sessionManager,
//                                                RedisCacheManager redisCacheManager) {
//        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
//        // 使用自己的realm
//        manager.setRealm(userRealm());
//        /*
//         * 关闭shiro自带的session，详情见文档
//         * http://shiro.apache.org/session-management.html#SessionManagement-
//         * StatelessApplications%28Sessionless%29
//         */
//        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
//        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
//        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
//        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
//        manager.setSubjectDAO(subjectDAO);
//        //inject sessionManager
//        manager.setSessionManager(sessionManager);
//
//        // inject redisCacheManager
//        manager.setCacheManager(redisCacheManager);
//        return manager;
//    }
//拦截路径
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterMap = new LinkedHashMap<>();

        filterMap.put("/**", "jwt");
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }

    @Bean({"shiroFilter", "shiroFilterFactoryBean"})
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
        System.out.println("------------------------------>执行请求过滤");
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new LinkedHashMap<>(Constant.Number.ONE);
        filterMap.put("jwt", jwtFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setUnauthorizedUrl("/401");
        /*
         * 自定义url规则 http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new LinkedHashMap<>(Constant.Number.TWO);
        filterRuleMap.put("/auth/login", "anon");
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/**", "jwt");
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        filterRuleMap.put("/404", "anon");
        filterRuleMap.put("/500", "anon");
        filterRuleMap.put("/error", "anon");

//        filterRuleMap.put("/v2/api-docs", "anon");
//        filterRuleMap.put("/v3/api-docs", "anon");
//        filterRuleMap.put("/webjars/**", "anon");
//        filterRuleMap.put("/swagger-resources/**", "anon");
//        filterRuleMap.put("/swagger-ui.html", "anon");
//        filterRuleMap.put("/doc.html", "anon");

        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    public JWTFilter2 jwtFilter() {
        return new JWTFilter2();
    }
}
