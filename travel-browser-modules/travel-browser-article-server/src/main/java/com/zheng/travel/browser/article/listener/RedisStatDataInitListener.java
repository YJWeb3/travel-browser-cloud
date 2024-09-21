package com.zheng.travel.browser.article.listener;

import com.zheng.travel.browser.article.domain.Strategy;
import com.zheng.travel.browser.article.redis.key.StrategyRedisKeyPrefix;
import com.zheng.travel.browser.article.service.StrategyService;
import com.zheng.travel.browser.redis.utils.RedisCache;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RedisStatDataInitListener implements ApplicationListener<ContextRefreshedEvent> {

    private final RedisCache redisCache;
    private final StrategyService strategyService;

    public RedisStatDataInitListener(StrategyService strategyService, RedisCache redisCache) {
        this.strategyService = strategyService;
        this.redisCache = redisCache;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        if (AnnotationConfigServletWebServerApplicationContext.class == ctx.getClass()) {
            System.out.println("----------------容器启动完成, 执行初始化数据----------------------");
            // 数据初始化
            // 1. 查询所有攻略数据
            // TODO 不能一次加载表中所有数据, 这样的操作在项目前期是不会有问题的, 但是最终会变成一个隐藏的大bug, 到项目后期数据量大了以后就会爆发出来
            List<Strategy> strategies = strategyService.list();
            System.out.println("[攻略统计数据初始化]");
            System.out.println("攻略数: " + strategies.size());
            int count = 0;
            // 2. 遍历攻略列表, 判断当前对象在 Redis 中是否已经存在
            for (Strategy strategy : strategies) {
                String fullKey = StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(strategy.getId() + "");
                Boolean exists = redisCache.hasKey(fullKey);
                if (!exists) {
                    // 3. 如果不存在, 才将数据存入 Redis
                    Map<String, Object> map = new HashMap<>();
                    map.put("viewnum", strategy.getViewnum());
                    map.put("replynum", strategy.getReplynum());
                    map.put("favornum", strategy.getFavornum());
                    map.put("sharenum", strategy.getSharenum());
                    map.put("thumbsupnum", strategy.getThumbsupnum());
                    redisCache.setCacheMap(fullKey, map);
                    count++;
                }
            }
            System.out.println("初始化: " + count);
            System.out.println("-----------------------数据初始化完成--------------------------");
        }
    }
}
