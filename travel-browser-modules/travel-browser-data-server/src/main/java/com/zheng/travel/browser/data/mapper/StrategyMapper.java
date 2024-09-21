package com.zheng.travel.browser.data.mapper;

import com.zheng.travel.browser.article.domain.Strategy;
import com.zheng.travel.browser.article.domain.StrategyRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface StrategyMapper extends BaseMapper<Strategy> {

    List<StrategyRank> selectStrategyRankByAbroad(Integer abroad);

    List<StrategyRank> selectStrategyRankHotList();
}
