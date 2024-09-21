package com.zheng.travel.browser.data.job;

import com.zheng.travel.browser.article.domain.Strategy;
import com.zheng.travel.browser.article.domain.StrategyRank;
import com.zheng.travel.browser.data.mapper.StrategyMapper;
import com.zheng.travel.browser.data.mapper.StrategyRankMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * 攻略排行数据统计任务
 */
@Slf4j
@Component
public class StrategyRankStatisJob {

    private final StrategyMapper strategyMapper;
    private final StrategyRankMapper strategyRankMapper;

    public StrategyRankStatisJob(StrategyMapper strategyMapper, StrategyRankMapper strategyRankMapper) {
        this.strategyMapper = strategyMapper;
        this.strategyRankMapper = strategyRankMapper;
    }

    @Transactional(rollbackFor = Exception.class)
     @Scheduled(cron = "0 */10 * * * *")
//    @Scheduled(cron = "0 * * * * *")
    public void statisRank() {
        log.info("[攻略排行数据统计] 排行数据统计开始 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Date now = new Date();
        // 统计国内攻略
        this.doStatis(now, StrategyRank.TYPE_CHINA, () -> strategyMapper.selectStrategyRankByAbroad(Strategy.ABROAD_NO));

        // 统计国外攻略
        this.doStatis(now, StrategyRank.TYPE_ABROAD, () -> strategyMapper.selectStrategyRankByAbroad(Strategy.ABROAD_YES));

        // 统计热门攻略
        this.doStatis(now, StrategyRank.TYPE_HOT, strategyMapper::selectStrategyRankHotList);

        // 删除这一次之前的所有数据
        strategyRankMapper.delete(new QueryWrapper<StrategyRank>().lt("UNIX_TIMESTAMP(statis_time)", (now.getTime() - 500) / 1000));
        log.info("[攻略排行数据统计] 排行数据统计结束, 删除旧的排行数据 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    public void doStatis(Date now, Integer type, Supplier<List<StrategyRank>> rankSupplier) {
        List<StrategyRank> strategyRanks = rankSupplier.get();
        log.info("[攻略排行数据统计] 排行数据统计: type={}, ranks={}", type, strategyRanks.size());
        for (StrategyRank rank : strategyRanks) {
            rank.setType(type);
            rank.setStatisTime(now);
        }
        // 保存到排名表中
        strategyRankMapper.batchInsert(strategyRanks);
    }
}
