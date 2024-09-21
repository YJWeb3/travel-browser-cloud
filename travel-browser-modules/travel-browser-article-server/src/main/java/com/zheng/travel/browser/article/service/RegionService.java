package com.zheng.travel.browser.article.service;

import com.zheng.travel.browser.article.domain.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RegionService extends IService<Region> {

    List<Region> findHotList();
}
