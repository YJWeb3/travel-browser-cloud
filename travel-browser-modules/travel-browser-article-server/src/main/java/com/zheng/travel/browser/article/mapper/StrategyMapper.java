package com.zheng.travel.browser.article.mapper;

import com.zheng.travel.browser.article.domain.Strategy;
import com.zheng.travel.browser.article.domain.StrategyCatalog;
import com.zheng.travel.browser.article.vo.StrategyCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface StrategyMapper extends BaseMapper<Strategy> {

    List<StrategyCatalog> selectGroupsByDestId(Long destId);

    List<StrategyCondition> selectDestCondition(int abroad);

    List<StrategyCondition> selectThemeCondition();
}
