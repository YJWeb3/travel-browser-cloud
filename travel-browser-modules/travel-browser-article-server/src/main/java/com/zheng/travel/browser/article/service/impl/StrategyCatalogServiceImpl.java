package com.zheng.travel.browser.article.service.impl;

import com.zheng.travel.browser.article.domain.StrategyCatalog;
import com.zheng.travel.browser.article.mapper.StrategyCatalogMapper;
import com.zheng.travel.browser.article.service.StrategyCatalogService;
import com.zheng.travel.browser.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog> implements StrategyCatalogService {

    @Override
    public List<StrategyCatalogGroup> findGroupList() {
        return getBaseMapper().selectGroupList();
    }
}
