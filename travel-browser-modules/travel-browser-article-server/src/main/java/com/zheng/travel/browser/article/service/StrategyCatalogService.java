package com.zheng.travel.browser.article.service;

import com.zheng.travel.browser.article.domain.StrategyCatalog;
import com.zheng.travel.browser.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StrategyCatalogService extends IService<StrategyCatalog> {

    List<StrategyCatalogGroup> findGroupList();
}
