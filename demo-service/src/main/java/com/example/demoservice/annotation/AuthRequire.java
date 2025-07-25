package com.example.demoservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/24
 * *@Version 1.0
 **/

@Target(ElementType.METHOD)  // 只能用在方法上
@Retention(RetentionPolicy.RUNTIME)  // 运行时生效
public @interface RequiresAuth {

    /**
     * 资源标识 (如: order, payment, user)
     */
    String resource();

    /**
     * 操作类型 (如: read, write, delete)
     */
    String action();

    /**
     * 是否需要资源ID (默认需要)
     */
    boolean requiresResourceId() default true;

    /**
     * 资源ID参数名 (默认从路径变量获取)
     */
    String resourceIdParam() default "";

    /**
     * 策略 (AND/OR 用于多权限检查)
     */
    Policy policy() default Policy.AND;

    enum Policy {
        AND, OR
    }
}
