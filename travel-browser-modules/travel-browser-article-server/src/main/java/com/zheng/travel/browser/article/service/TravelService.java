package com.zheng.travel.browser.article.service;

import com.zheng.travel.browser.article.domain.Travel;
import com.zheng.travel.browser.article.qo.TravelQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TravelService extends IService<Travel> {

    Page<Travel> pageList(TravelQuery query);
}
