package com.zheng.travel.browser.data.mapper;

import com.zheng.travel.browser.article.domain.StrategyRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface StrategyRankMapper extends BaseMapper<StrategyRank> {
    void batchInsert(List<StrategyRank> strategyRanks);
}
