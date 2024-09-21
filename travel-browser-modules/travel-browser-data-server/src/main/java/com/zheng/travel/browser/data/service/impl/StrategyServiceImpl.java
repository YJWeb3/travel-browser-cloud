package com.zheng.travel.browser.data.service.impl;

import com.zheng.travel.browser.article.domain.Strategy;
import com.zheng.travel.browser.data.mapper.StrategyMapper;
import com.zheng.travel.browser.data.service.StrategyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
}
