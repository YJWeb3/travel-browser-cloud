package com.zheng.travel.browser.article.service.impl;

import com.zheng.travel.browser.article.domain.Destination;
import com.zheng.travel.browser.article.domain.Region;
import com.zheng.travel.browser.article.mapper.DestinationMapper;
import com.zheng.travel.browser.article.qo.DestinationQuery;
import com.zheng.travel.browser.article.service.DestinationService;
import com.zheng.travel.browser.article.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {

    private final RegionService regionService;

    public DestinationServiceImpl(RegionService regionService) {
        this.regionService = regionService;
    }

    @Override
    public List<Destination> getDestinationByRegionId(Long regionId) {
        // 1. 基于区域 id 查询区域对象
        Region region = regionService.getById(regionId);
        if (region == null) {
            return Collections.emptyList();
        }
        // 2. 基于区域对象, 得到目的地 id 集合
        List<Long> ids = region.parseRefIds();
        if (ids.size() == 0) {
            return Collections.emptyList();
        }
        // 3. 基于目的地 id 集合查询目的地集合并返回
        return super.listByIds(ids);
    }

    @Override
    public Page<Destination> pageList(DestinationQuery query) {
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        // 如果 parentId 为 null 就查询所有 parentId IS NULL 的数据
        wrapper.isNull(query.getParentId() == null, "parent_id");
        // 如果 parentId 不为 null, 就根据 parentId 进行查询
        wrapper.eq(query.getParentId() != null, "parent_id", query.getParentId());
        // 关键字查询
        wrapper.like(!StringUtils.isEmpty(query.getKeyword()), "name", query.getKeyword());
        return super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);
    }

    @Override
    public List<Destination> findToasts(Long destId) {
        List<Destination> destinations = new ArrayList<>();
        while (destId != null) {
            Destination dest = super.getById(destId);
            if (dest == null) {
                break;
            }
            destinations.add(dest);
            destId = dest.getParentId();
        }
        Collections.reverse(destinations);
        return destinations;
    }

    @Override
    public List<Destination> findDestsByRid(Long rid) {
        List<Destination> destinations;
        // 查询国内的数据
        if (rid < 0) {
            destinations = this.getBaseMapper().selectHotListByRid(rid, Collections.emptyList());
        } else {
            // 查询其他区域的数据
            Region region = regionService.getById(rid);
            if (region == null) {
                return Collections.emptyList();
            }
            destinations = this.getBaseMapper().selectHotListByRid(rid, region.parseRefIds());
        }

        // 对每一个子目的地集合做裁剪, 只保留10条数据
        for (Destination destination : destinations) {
            List<Destination> children = destination.getChildren();
            if (children != null && children.size() > 10) {
                destination.setChildren(children.subList(0, 10));
            }
        }

        return destinations;
    }
}
