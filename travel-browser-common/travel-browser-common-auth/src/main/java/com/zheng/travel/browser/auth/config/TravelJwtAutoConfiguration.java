package com.zheng.travel.browser.auth.config;

import com.zheng.travel.browser.auth.interceptor.LoginInterceptor;
import com.zheng.travel.browser.auth.util.SpringContextUtil;
import com.zheng.travel.browser.redis.utils.RedisCache;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
/**
 * \@Import 注解等同于以前 xml 文件中的 \<import resource="applicationContext.xml"/>
 */
@Import(WebConfig.class)
@EnableConfigurationProperties(JwtProperties.class)
public class TravelJwtAutoConfiguration {

    @Bean
    public LoginInterceptor loginInterceptor(RedisCache redisCache, JwtProperties jwtProperties) {
        return new LoginInterceptor(redisCache, jwtProperties);
    }

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
