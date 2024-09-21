package com.zheng.travel.browser.article.service;

import com.zheng.travel.browser.article.domain.Destination;
import com.zheng.travel.browser.article.qo.DestinationQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DestinationService extends IService<Destination> {

    List<Destination> getDestinationByRegionId(Long id);

    Page<Destination> pageList(DestinationQuery query);

    List<Destination> findToasts(Long destId);

    List<Destination> findDestsByRid(Long rid);
}
