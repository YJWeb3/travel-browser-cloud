package com.zheng.travel.browser.article.service;

import com.zheng.travel.browser.article.domain.Strategy;
import com.zheng.travel.browser.article.domain.StrategyCatalog;
import com.zheng.travel.browser.article.domain.StrategyContent;
import com.zheng.travel.browser.article.qo.StrategyQuery;
import com.zheng.travel.browser.article.vo.StrategyCondition;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface StrategyService extends IService<Strategy> {

    List<StrategyCatalog> findGroupsByDestId(Long destId);

    StrategyContent getContentById(Long id);

    List<Strategy> findViewnumTop3ByDestId(Long destId);

    Page<Strategy> pageList(StrategyQuery qo);

    List<StrategyCondition> findDestCondition(int abroad);

    List<StrategyCondition> findThemeCondition();

    void viewnumIncr(Long id);

    boolean thumbnumIncr(Long sid);

    Map<String, Object> getStatData(Long id);
}
