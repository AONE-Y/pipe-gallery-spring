package com.hainu.common.aop;

import com.alibaba.fastjson.JSON;
import com.hainu.common.annotation.Cach;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
class AopCachHandle {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut(value = "@annotation(com.hainu.common.annotation.Cach)")
    public void pointcut() {

    }

    @Around(value = "pointcut() && @annotation(cach)")
    public Object around(ProceedingJoinPoint point, Cach cach) {

        Method method = getMethod(point);
        //根据类名、方法名和参数生成key
        Class classType = cach.type();
        if (classType == Object.class) {
            classType=method.getReturnType();
        }

        final String key = parseKey(cach.key(), method, point.getArgs());

        String value = stringRedisTemplate.opsForValue().get(key);

        if (null != value) {

            return JSON.parseObject(value, classType);

        }

        try {

            Object proceed = point.proceed();

            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(proceed), cach.expire(), TimeUnit.SECONDS);

            return proceed;

        } catch (Throwable throwable) {

            throwable.printStackTrace();

        }

        return null;

    }

    /**
     * 获取被拦截方法对象
     * <p>
     * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象
     * <p>
     * 而缓存的注解在实现类的方法上
     * <p>
     * 所以应该使用反射获取当前对象的方法对象
     */

    private Method getMethod(ProceedingJoinPoint pjp) {

        //获取参数的类型

        Class[] argTypes = ((MethodSignature) pjp.getSignature()).getParameterTypes();

        Method method = null;

        try {

            method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);

        } catch (NoSuchMethodException e) {

            e.printStackTrace();

        }

        return method;

    }

    private String parseKey(String key, Method method, Object[] args) {

        if (ObjectUtils.isEmpty(key)) {

            return method.getName();

        }

        //获得被拦截方法参数列表

        LocalVariableTableParameterNameDiscoverer nd = new LocalVariableTableParameterNameDiscoverer();

        String[] parameterNames = nd.getParameterNames(method);

        for (int i = 0; i < parameterNames.length; i++) {

            key = key.replace(parameterNames[i] + "", args[i] + "");

        }

        return method.getName() + key;

    }

}
