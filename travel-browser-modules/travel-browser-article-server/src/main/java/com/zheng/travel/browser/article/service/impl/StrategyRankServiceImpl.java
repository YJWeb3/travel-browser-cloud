package com.zheng.travel.browser.article.service.impl;

import com.zheng.travel.browser.article.domain.StrategyRank;
import com.zheng.travel.browser.article.mapper.StrategyRankMapper;
import com.zheng.travel.browser.article.service.StrategyRankService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyRankServiceImpl extends ServiceImpl<StrategyRankMapper, StrategyRank> implements StrategyRankService {

    @Override
    public List<StrategyRank> selectLastRanksByType(int type) {
        QueryWrapper<StrategyRank> wrapper = new QueryWrapper<StrategyRank>()
                .eq("type", type)
                .orderByDesc("statis_time", "statisnum")
                .last("limit 10");
        return list(wrapper);
    }
}
