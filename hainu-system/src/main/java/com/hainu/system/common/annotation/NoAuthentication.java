package com.hainu.system.common.annotation;

import java.lang.annotation.*;

/**
 * 
 * @ClassName: Pass
 * @Description: 在Controller方法上加入该注解不会验证身份
 * @author ANONE
 * @date 2021年8月22日 下午11:52:07
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuthentication {
}
