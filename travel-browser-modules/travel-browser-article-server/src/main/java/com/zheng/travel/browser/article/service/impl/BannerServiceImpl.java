package com.zheng.travel.browser.article.service.impl;

import com.zheng.travel.browser.article.domain.Banner;
import com.zheng.travel.browser.article.mapper.BannerMapper;
import com.zheng.travel.browser.article.service.BannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public List<Banner> findByType(Integer type) {
        return list(
                new QueryWrapper<Banner>()
                        .eq("type", type)
                        .eq("state", Banner.STATE_NORMAL)
                        .orderByAsc("seq")
        );
    }
}
