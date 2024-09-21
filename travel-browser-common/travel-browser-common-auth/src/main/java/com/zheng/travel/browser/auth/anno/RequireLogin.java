package com.zheng.travel.browser.auth.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能: 标识接口需要登录才能访问, 可以贴在类或方法上, 如果贴在类上, 表示整个类中所有接口都需要登录才能访问
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireLogin {
}
