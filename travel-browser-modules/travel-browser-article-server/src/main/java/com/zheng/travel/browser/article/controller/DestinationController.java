package com.zheng.travel.browser.article.controller;

import com.zheng.travel.browser.article.domain.Destination;
import com.zheng.travel.browser.article.qo.DestinationQuery;
import com.zheng.travel.browser.article.service.DestinationService;
import com.zheng.travel.browser.core.qo.QueryObject;
import com.zheng.travel.browser.core.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public R<Page<Destination>> pageList(DestinationQuery query) {
        return R.ok(destinationService.pageList(query));
    }

    @PostMapping("/search")
    public R<List<Destination>> searchList(@RequestBody QueryObject qo) {
        return R.ok(destinationService.list(new QueryWrapper<Destination>().last("limit " + qo.getOffset() + ", " + qo.getSize())));
    }

    @GetMapping("/getByName")
    public R<Destination> getDestByName(@RequestParam String name) {
        return R.ok(destinationService.getOne(new QueryWrapper<Destination>().eq("name", name)));
    }

    @GetMapping("/toasts")
    public R<List<Destination>> toasts(Long destId) {
        return R.ok(destinationService.findToasts(destId));
    }

    @GetMapping("/hotList")
    public R<List<Destination>> hotList(Long rid) {
        return R.ok(destinationService.findDestsByRid(rid));
    }

    @GetMapping("/list")
    public R<List<Destination>> listAll() {
        return R.ok(destinationService.list());
    }

    @GetMapping("/detail")
    public R<Destination> getById(Long id) {
        return R.ok(destinationService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(Destination dest) {
        destinationService.save(dest);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(Destination dest) {
        destinationService.updateById(dest);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id) {
        destinationService.removeById(id);
        return R.ok();
    }
}
