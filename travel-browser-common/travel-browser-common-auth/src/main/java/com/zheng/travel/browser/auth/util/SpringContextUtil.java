package com.zheng.travel.browser.auth.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring工具类,获取Spring上下文对象等
 *
 * @author zhenhgge
 * @since 2023/11/08 21:11
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    /**
     * 1. SpringContextUtil 被 JVM 加载时, applicationContext 作为静态属性, 就被初始化了, 但是此时是 null 值
     * 2. 当 Spring 容器初始化以后, 会管理 SpringContextUtil Bean 对象
     * 3. 当 Spring 创建 SpringContextUtil 实例对象时,
     * 在初始化阶段会自动调用实现了 ApplicationContextAware 的 setApplicationContext 方法,
     * 此时该类中原本静态容器属性就从 null 变成了容器对象
     * 4. 当容器启动成功后, 其他业务代码通过该类的静态成员, 就可以直接的访问容器对象, 从容器对象中获取其他 Bean 对象
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}