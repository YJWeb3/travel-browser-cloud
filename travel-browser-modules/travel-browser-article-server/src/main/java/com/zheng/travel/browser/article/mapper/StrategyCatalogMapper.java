package com.zheng.travel.browser.article.mapper;

import com.zheng.travel.browser.article.domain.StrategyCatalog;
import com.zheng.travel.browser.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface StrategyCatalogMapper extends BaseMapper<StrategyCatalog> {

    List<StrategyCatalogGroup> selectGroupList();
}
