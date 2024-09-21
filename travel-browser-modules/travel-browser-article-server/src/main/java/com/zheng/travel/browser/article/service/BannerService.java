package com.zheng.travel.browser.article.service;

import com.zheng.travel.browser.article.domain.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BannerService extends IService<Banner> {

    List<Banner> findByType(Integer type);
}
