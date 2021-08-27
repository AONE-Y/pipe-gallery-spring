package com.hainu.common.annotation;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.common.annotation
 * @Date：2021/8/27 18:23
 * @Author：yy188
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: yy188
 */
import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cach {
    String key() default "";

    Class type() default Object.class;

    //默认缓存时间是一天

    long expire() default 60*60*24L;
}
