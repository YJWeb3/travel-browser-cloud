package com.zheng.travel.browser.article.controller;

import com.zheng.travel.browser.article.domain.StrategyTheme;
import com.zheng.travel.browser.article.service.StrategyThemeService;
import com.zheng.travel.browser.core.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strategies/themes")
public class StrategyThemeController {

    private final StrategyThemeService strategyThemeService;

    public StrategyThemeController(StrategyThemeService strategyThemeService) {
        this.strategyThemeService = strategyThemeService;
    }

    @GetMapping("/query")
    public R<Page<StrategyTheme>> pageList(Page<StrategyTheme> page) {
        return R.ok(strategyThemeService.page(page));
    }

    @GetMapping("/detail")
    public R<StrategyTheme> getById(Long id) {
        return R.ok(strategyThemeService.getById(id));
    }

    @GetMapping("/list")
    public R<List<StrategyTheme>> listAll() {
        return R.ok(strategyThemeService.list());
    }

    @PostMapping("/save")
    public R<?> save(StrategyTheme dest) {
        strategyThemeService.save(dest);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(StrategyTheme dest) {
        strategyThemeService.updateById(dest);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id) {
        strategyThemeService.removeById(id);
        return R.ok();
    }
}
