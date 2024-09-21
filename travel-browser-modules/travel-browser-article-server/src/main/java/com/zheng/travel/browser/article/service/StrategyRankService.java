package com.zheng.travel.browser.article.service;

import com.zheng.travel.browser.article.domain.StrategyRank;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StrategyRankService extends IService<StrategyRank> {

    List<StrategyRank> selectLastRanksByType(int type);
}
