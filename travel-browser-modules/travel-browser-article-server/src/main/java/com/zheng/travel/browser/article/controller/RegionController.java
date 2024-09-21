package com.zheng.travel.browser.article.controller;

import com.zheng.travel.browser.article.domain.Destination;
import com.zheng.travel.browser.article.domain.Region;
import com.zheng.travel.browser.article.service.DestinationService;
import com.zheng.travel.browser.article.service.RegionService;
import com.zheng.travel.browser.core.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;
    private final DestinationService destinationService;

    public RegionController(RegionService regionService, DestinationService destinationService) {
        this.regionService = regionService;
        this.destinationService = destinationService;
    }

    @GetMapping
    public R<Page<Region>> pageList(Page<Region> page) {
        return R.ok(regionService.page(page));
    }

    @GetMapping("/detail")
    public R<Region> getById(Long id) {
        return R.ok(regionService.getById(id));
    }

    @GetMapping("/hotList")
    public R<List<Region>> hotList() {
        return R.ok(regionService.findHotList());
    }

    @PostMapping("/save")
    public R<?> save(Region dest) {
        regionService.save(dest);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(Region dest) {
        regionService.updateById(dest);
        return R.ok();
    }

    @GetMapping("/{id}/destination")
    public R<List<Destination>> getDestinationByRegionId(@PathVariable Long id) {
        List<Destination> destinations = destinationService.getDestinationByRegionId(id);
        return R.ok(destinations);
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id) {
        regionService.removeById(id);
        return R.ok();
    }
}
