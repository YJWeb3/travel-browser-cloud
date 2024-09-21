package com.zheng.travel.browser.article.service.impl;

import com.zheng.travel.browser.article.domain.Region;
import com.zheng.travel.browser.article.mapper.RegionMapper;
import com.zheng.travel.browser.article.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    @Override
    public List<Region> findHotList() {
        // 查询所有热门区域, 并且进行排序
        return list(
                new QueryWrapper<Region>()
                        .eq("ishot", Region.STATE_HOT)
                        .orderByAsc("seq")
        );
    }
}
