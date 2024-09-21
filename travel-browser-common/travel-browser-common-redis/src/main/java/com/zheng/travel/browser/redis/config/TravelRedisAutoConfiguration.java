package com.zheng.travel.browser.redis.config;

import com.zheng.travel.browser.redis.utils.RedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 如果从 Spring 容器中找不到 RedisConnectionFactory 类型的 Bean 对象
 * 那么这整个配置对象都不会生效
 */
@ConditionalOnMissingBean(RedisConnectionFactory.class)
@Configuration
public class TravelRedisAutoConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 设置 Redis key 的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        // 设置 hash key 的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置 value 序列化方式
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }

    @Bean
    public RedisCache redisCache(RedisConnectionFactory factory) {
        return new RedisCache(redisTemplate(factory));
    }
}
