package com.zheng.travel.browser.article.controller;

import com.zheng.travel.browser.article.domain.StrategyCatalog;
import com.zheng.travel.browser.article.service.StrategyCatalogService;
import com.zheng.travel.browser.article.vo.StrategyCatalogGroup;
import com.zheng.travel.browser.core.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strategies/catalogs")
public class StrategyCatalogController {

    private final StrategyCatalogService strategyCatalogService;

    public StrategyCatalogController(StrategyCatalogService strategyCatalogService) {
        this.strategyCatalogService = strategyCatalogService;
    }

    @GetMapping("/query")
    public R<Page<StrategyCatalog>> pageList(Page<StrategyCatalog> page) {
        return R.ok(strategyCatalogService.page(page));
    }

    @GetMapping("/groups")
    public R<List<StrategyCatalogGroup>> groupList() {
        return R.ok(strategyCatalogService.findGroupList());
    }

    @GetMapping("/detail")
    public R<StrategyCatalog> getById(Long id) {
        return R.ok(strategyCatalogService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(StrategyCatalog dest) {
        strategyCatalogService.save(dest);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(StrategyCatalog dest) {
        strategyCatalogService.updateById(dest);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id) {
        strategyCatalogService.removeById(id);
        return R.ok();
    }
}
