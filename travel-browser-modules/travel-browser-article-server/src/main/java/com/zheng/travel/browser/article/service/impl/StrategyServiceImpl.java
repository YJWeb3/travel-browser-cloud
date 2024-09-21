package com.zheng.travel.browser.article.service.impl;

import com.zheng.travel.browser.article.domain.*;
import com.zheng.travel.browser.article.feign.UserInfoFeignService;
import com.zheng.travel.browser.article.redis.key.StrategyRedisKeyPrefix;
import com.zheng.travel.browser.article.mapper.StrategyContentMapper;
import com.zheng.travel.browser.article.mapper.StrategyMapper;
import com.zheng.travel.browser.article.qo.StrategyQuery;
import com.zheng.travel.browser.article.service.DestinationService;
import com.zheng.travel.browser.article.service.StrategyCatalogService;
import com.zheng.travel.browser.article.service.StrategyService;
import com.zheng.travel.browser.article.service.StrategyThemeService;
import com.zheng.travel.browser.article.utils.OssUtil;
import com.zheng.travel.browser.article.vo.StrategyCondition;
import com.zheng.travel.browser.auth.util.AuthenticationUtils;
import com.zheng.travel.browser.core.utils.DateUtils;
import com.zheng.travel.browser.core.utils.R;
import com.zheng.travel.browser.redis.utils.RedisCache;
import com.zheng.travel.browser.user.vo.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {

    private final RedisCache redisCache;
    private final StrategyThemeService strategyThemeService;
    private final DestinationService destinationService;
    private final StrategyCatalogService strategyCatalogService;
    private final StrategyContentMapper strategyContentMapper;
    private final UserInfoFeignService userInfoFeignService;

    public StrategyServiceImpl(StrategyCatalogService strategyCatalogService, DestinationService destinationService,
                               StrategyThemeService strategyThemeService, StrategyContentMapper strategyContentMapper,
                               RedisCache redisCache,
            /* @Lazy 懒加载, 只有在该 Bean 对象被调用时, 才会创建, 可以解决循环依赖问题 */
                               @Lazy UserInfoFeignService userInfoFeignService) {
        this.strategyCatalogService = strategyCatalogService;
        this.destinationService = destinationService;
        this.strategyThemeService = strategyThemeService;
        this.strategyContentMapper = strategyContentMapper;
        this.redisCache = redisCache;
        this.userInfoFeignService = userInfoFeignService;
    }

    @Override
    public Strategy getById(Serializable id) {
        Strategy strategy = super.getById(id);
        StrategyContent content = strategyContentMapper.selectById(id);
        strategy.setContent(content);
        // 查询当前用户是否已收藏攻略
        LoginUser user = AuthenticationUtils.getUser();
        if (user != null) {
            R<List<Long>> favoriteStrategyIdList = userInfoFeignService.getFavorStrategyIdList(user.getId());
            List<Long> list = favoriteStrategyIdList.checkAndGet();
            strategy.setFavorite(list.contains(id));
        }
        // 从 redis 中查询最新的统计数据
        Map<String, Object> statData = redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
        if (statData != null) {
            strategy.setViewnum((Integer) statData.get("viewnum"));
            strategy.setReplynum((Integer) statData.get("replynum"));
            strategy.setFavornum((Integer) statData.get("favornum"));
            strategy.setSharenum((Integer) statData.get("sharenum"));
            strategy.setThumbsupnum((Integer) statData.get("thumbsupnum"));
        }
        return strategy;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Strategy entity) {
        return doSaveOrUpdate(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(Strategy entity) {
        return doSaveOrUpdate(entity);
    }

    private boolean doSaveOrUpdate(Strategy entity) {
        // 1. 完成封面图片的上传, 且得到 url 后重新设置到 cover 属性中
        if (!StringUtils.isEmpty(entity.getCoverUrl()) || !entity.getCoverUrl().startsWith("http")) {
            String fileName = UUID.randomUUID().toString();
            String url = OssUtil.uploadImgByBase64("images/strategies", fileName + ".jpg", entity.getCoverUrl());
            entity.setCoverUrl(url);
        }
        // 2. 补充分类名称
        StrategyCatalog catalog = strategyCatalogService.getById(entity.getCatalogId());
        entity.setCatalogName(catalog.getName());
        // 3. 根据分类中的目的地id/名称设置到攻略对象中
        entity.setDestId(catalog.getDestId());
        entity.setDestName(catalog.getDestName());
        // 4. 基于目的地判断是否是国外
        List<Destination> toasts = destinationService.findToasts(catalog.getDestId());
        Destination dest = toasts.get(0);
        if (dest.getId() == 1) {
            entity.setIsabroad(Strategy.ABROAD_NO);
        } else {
            entity.setIsabroad(Strategy.ABROAD_YES);
        }
        // 5. 查询主题, 设置主题名称
        StrategyTheme theme = strategyThemeService.getById(entity.getThemeId());
        entity.setThemeName(theme.getName());

        // 判断是更新还是新增
        if (entity.getId() == null) {
            // 6. 设置创建时间
            entity.setCreateTime(new Date());
            // 7. 设置各种数量为0
            entity.setViewnum(0);
            entity.setSharenum(0);
            entity.setThumbsupnum(0);
            entity.setReplynum(0);
            entity.setFavornum(0);
            // 8. 重新设置默认状态, 覆盖前端提交的值
            entity.setState(Strategy.STATE_NORMAL);
            // 9. 保存攻略对象, 得到攻略自增的 id
            boolean save = super.save(entity);
            // 10. 将攻略 id 设置到内容对象中, 并保存内容对象
            StrategyContent content = entity.getContent();
            content.setId(entity.getId());
            return save && strategyContentMapper.insert(content) > 0;
        }

        // 更新操作
        boolean ret = super.updateById(entity);
        StrategyContent content = entity.getContent();
        content.setId(entity.getId());
        int row = strategyContentMapper.updateById(entity.getContent());
        return ret && row > 0;
    }

    @Override
    public List<StrategyCatalog> findGroupsByDestId(Long destId) {
        return getBaseMapper().selectGroupsByDestId(destId);
    }

    @Override
    public StrategyContent getContentById(Long id) {
        return strategyContentMapper.selectById(id);
    }

    @Override
    public List<Strategy> findViewnumTop3ByDestId(Long destId) {
        // 查询指定目的地之下浏览数前三的攻略
        QueryWrapper<Strategy> wrapper = new QueryWrapper<Strategy>()
                .eq("dest_id", destId)
                .orderByDesc("viewnum")
                .last("limit 3");
        return list(wrapper);
    }

    @Override
    public Page<Strategy> pageList(StrategyQuery qo) {
        if ((qo.getType() != null && qo.getType() != -1) && (qo.getRefid() != null && qo.getRefid() != -1)) {
            if (qo.getType() == 3) {
                // 如果当前类型 == 3 就按照主题查询
                qo.setThemeId(qo.getRefid());
            } else {
                // 否则按照目的地查询
                qo.setDestId(qo.getRefid());
            }
        }

        QueryWrapper<Strategy> wrapper = new QueryWrapper<Strategy>()
                .eq(qo.getDestId() != null, "dest_id", qo.getDestId())
                .eq(qo.getThemeId() != null, "theme_id", qo.getThemeId())
                .orderByDesc(!StringUtils.isEmpty(qo.getOrderBy()), qo.getOrderBy());

        return super.page(new Page<>(qo.getCurrent(), qo.getSize()), wrapper);
    }

    @Override
    public List<StrategyCondition> findDestCondition(int abroad) {
        return getBaseMapper().selectDestCondition(abroad);
    }

    @Override
    public List<StrategyCondition> findThemeCondition() {
        return getBaseMapper().selectThemeCondition();
    }

    @Override
    public void viewnumIncr(Long id) {
        this.statDataIncr("viewnum", id);
    }

    @Override
    public boolean thumbnumIncr(Long sid) {
        LoginUser user = AuthenticationUtils.getUser();
        // 1. 查询 redis 记录, 判断是否已经置顶过, 如果有直接抛出异常
        StrategyRedisKeyPrefix keyPrefix = StrategyRedisKeyPrefix.STRATEGIES_TOP_MAP;
        String fullKey = keyPrefix.fullKey(sid + "");
        // TODO 如果此时同一个用户非常快速的访问了多次当前接口, 在此处会不会被拦截?
        // TODO 如果不会, 怎么解决?
        /*Integer count = redisCache.getCacheMapValue(fullKey, user.getId() + "");
        if (count != null && count > 0) {
            return false;
        }*/
        // 2. 否则先记录当前用户已经针对该文章置顶, 并设置过期时间为今天最后一秒
        // 获取到从当前时间开始->今天的最后一秒
        keyPrefix.setTimeout(DateUtils.getLastMillisSeconds());
        keyPrefix.setUnit(TimeUnit.MILLISECONDS);
        Long ret = redisCache.hashIncrement(keyPrefix, user.getId() + "",
                1, sid + "");
        if (ret > 1) {
            // 说明之前已经存过值了
            return false;
        }
        // 3. 置顶数+1
        this.statDataIncr("thumbsupnum", sid);
        return true;
    }

    private void statDataIncr(String hashKey, Long sid) {
        redisCache.hashIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, hashKey, 1, sid + "");
        // 记录操作次数
        redisCache.zsetIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 1, sid);
    }

    @Override
    public Map<String, Object> getStatData(Long id) {
        return redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
    }
}
