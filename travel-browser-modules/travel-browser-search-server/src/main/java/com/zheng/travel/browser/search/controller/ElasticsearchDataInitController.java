package com.zheng.travel.browser.search.controller;

import com.zheng.travel.browser.core.exception.BusinessException;
import com.zheng.travel.browser.core.qo.QueryObject;
import com.zheng.travel.browser.core.utils.R;
import com.zheng.travel.browser.redis.utils.RedisCache;
import com.zheng.travel.browser.search.domain.DestinationEs;
import com.zheng.travel.browser.search.domain.StrategyEs;
import com.zheng.travel.browser.search.domain.TravelEs;
import com.zheng.travel.browser.search.domain.UserInfoEs;
import com.zheng.travel.browser.search.feign.ArticleFeignService;
import com.zheng.travel.browser.search.feign.UserInfoFeignService;
import com.zheng.travel.browser.search.service.ElasticsearchService;
import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@ApiIgnore
@RestController
@RequestMapping("/init")
@RefreshScope
public class ElasticsearchDataInitController {

    public static final String INIT_USER = "user";
    public static final String INIT_TRAVEL = "travel";
    public static final String INIT_STRATEGY = "strategy";
    public static final String INIT_DESTINATION = "destination";
    public static final Integer BATCH_COUNT = 200;

    private final Map<String, EsDataInitStrategy> DATA_HANDLER_STRATEGY_MAP = new HashMap<>();

    @Value("${es.init.key}")
    private String initKey;

    private final ArticleFeignService articleFeignService;
    private final ElasticsearchService elasticsearchService;
    private final UserInfoFeignService userInfoFeignService;
    private final RedisCache redisCache;

    public ElasticsearchDataInitController(RedisCache redisCache,
                                           UserInfoFeignService userInfoFeignService,
                                           ElasticsearchService elasticsearchService,
                                           ArticleFeignService articleFeignService) {
        this.redisCache = redisCache;
        this.userInfoFeignService = userInfoFeignService;
        this.elasticsearchService = elasticsearchService;
        this.articleFeignService = articleFeignService;
    }

    @Getter
    @Setter
    static class EsDataInitStrategy {
        private Function<QueryObject, R<List<Object>>> function;
        private Class<?> clazz;

        public EsDataInitStrategy(Function<QueryObject, R<List<Object>>> function, Class<?> clazz) {
            this.function = function;
            this.clazz = clazz;
        }
    }

    @PostConstruct
    public void postConstruct() {
        // 用户初始化
        EsDataInitStrategy userDataInitStrategy
                = new EsDataInitStrategy(qo -> userInfoFeignService.findList(qo.getCurrent(), qo.getSize()), UserInfoEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_USER, userDataInitStrategy);

        // 游记初始化
        EsDataInitStrategy travelDataInitStrategy
                = new EsDataInitStrategy(articleFeignService::travelSearchList, TravelEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_TRAVEL, travelDataInitStrategy);

        // 攻略初始化
        EsDataInitStrategy strategyDataInitStrategy
                = new EsDataInitStrategy(articleFeignService::strategySearchList, StrategyEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_STRATEGY, strategyDataInitStrategy);

        // 目的地初始化
        EsDataInitStrategy destinationDataInitStrategy
                = new EsDataInitStrategy(articleFeignService::destinationSearchList, DestinationEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_DESTINATION, destinationDataInitStrategy);
    }

    @GetMapping("/{key}/{type}")
    public ResponseEntity<?> init(@PathVariable String key, @PathVariable String type) {
        log.info("[ES 数据初始化] -----------------------数据初始化开始-----------------------");
        if (StringUtils.isEmpty(key) || !initKey.equals(key)) {
            log.warn("[ES 数据初始化] 非法操作, 请求参数有误 key={}, type={}, initKey={}", key, type, initKey);
            // 如果传入的参数有误, 不是配置文件中配置的路径, 就不允许访问
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 用户访问过, 就不允许再访问了
        String redisKey = "es:init:" + key + ":" + type;
        Boolean ret = redisCache.setnx(redisKey, "inited");
        if (ret == null || !ret) {
            log.warn("[ES 数据初始化] 非法操作, 之前已经初始化过了 redisKey={}, ret={}", redisKey, ret);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 开始初始化数据
        this.doInit(type);

        log.info("[ES 数据初始化] -----------------------数据初始化结束-----------------------");
        return ResponseEntity.ok().body("init success");
    }

    private void doInit(String type) {
        List<Object> dataList = null;
        int current = 1;
        do {
            // 获取并处理远程数据
            dataList = this.handleRemoteDataList(current++, type);
            if (dataList == null || dataList.size() == 0) {
                // 执行完成, 结束循环
                log.info("[ES 数据初始化] 数据初始化完成, 查询不到新的数据...");
                return;
            }

            // 批量保存数据
            elasticsearchService.save(dataList);
        } while (true);
    }

    private List<Object> handleRemoteDataList(Integer current, String type) {
        EsDataInitStrategy strategy = DATA_HANDLER_STRATEGY_MAP.get(type);
        if (strategy == null) {
            throw new BusinessException("初始化参数类型错误！");
        }

        R<List<Object>> ret = strategy.getFunction().apply(new QueryObject(current, BATCH_COUNT));
        log.info("[ES 数据初始化] 初始化开始, 查询{}数据 data={}", type, JSON.toJSONString(ret));
        List<Object> list = ret.checkAndGet();
        if (list == null || list.size() == 0) {
            return list;
        }

        try {
            List<Object> dataList = new ArrayList<>(list.size());
            Class<?> clazz = strategy.getClazz();

            for (Object dto : list) {
                Object es = clazz.newInstance();
                BeanUtils.copyProperties(es, dto);
                dataList.add(es);
            }

            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
