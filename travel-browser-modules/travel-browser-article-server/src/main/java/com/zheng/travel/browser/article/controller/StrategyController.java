package com.zheng.travel.browser.article.controller;

import com.zheng.travel.browser.article.domain.Strategy;
import com.zheng.travel.browser.article.domain.StrategyCatalog;
import com.zheng.travel.browser.article.domain.StrategyContent;
import com.zheng.travel.browser.article.domain.StrategyRank;
import com.zheng.travel.browser.article.qo.StrategyQuery;
import com.zheng.travel.browser.article.service.StrategyRankService;
import com.zheng.travel.browser.article.service.StrategyService;
import com.zheng.travel.browser.article.utils.OssUtil;
import com.zheng.travel.browser.article.vo.StrategyCondition;
import com.zheng.travel.browser.auth.anno.RequireLogin;
import com.zheng.travel.browser.core.qo.QueryObject;
import com.zheng.travel.browser.core.utils.R;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/strategies")
public class StrategyController {

    private final StrategyService strategyService;
    private final StrategyRankService strategyRankService;

    public StrategyController(StrategyService strategyService, StrategyRankService strategyRankService) {
        this.strategyService = strategyService;
        this.strategyRankService = strategyRankService;
    }

    @PostMapping("/search")
    public R<List<Strategy>> searchList(@RequestBody QueryObject qo) {
        return R.ok(strategyService.list(new QueryWrapper<Strategy>().last("limit " + qo.getOffset() + ", " + qo.getSize())));
    }

    @GetMapping("/findByDestName")
    public R<List<Strategy>> findByDestName(@RequestParam String destName) {
        return R.ok(strategyService.list(new QueryWrapper<Strategy>().eq("dest_name", destName)));
    }

    @GetMapping("/stat/data")
    public R<Map<String, Object>> getStatData(Long id) {
        Map<String, Object> ret = strategyService.getStatData(id);
        return R.ok(ret);
    }

    @RequireLogin
    @GetMapping("/thumbnumIncr")
    public R<Boolean> thumbnumIncr(Long sid) {
        boolean ret = strategyService.thumbnumIncr(sid);
        return R.ok(ret);
    }

    @GetMapping("/query")
    public R<Page<Strategy>> pageList(StrategyQuery qo) {
        return R.ok(strategyService.pageList(qo));
    }

    @GetMapping("/groups")
    public R<List<StrategyCatalog>> groupByCatalog(Long destId) {
        return R.ok(strategyService.findGroupsByDestId(destId));
    }

    @GetMapping("/ranks")
    public R<JSONObject> findRanks() {
        List<StrategyRank> abroadRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_ABROAD);
        List<StrategyRank> chinaRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_CHINA);
        List<StrategyRank> hotRank = strategyRankService.selectLastRanksByType(StrategyRank.TYPE_HOT);

        JSONObject result = new JSONObject();
        result.put("abroadRank", abroadRank);
        result.put("chinaRank", chinaRank);
        result.put("hotRank", hotRank);
        return R.ok(result);
    }

    @GetMapping("/viewnumTop3")
    public R<List<Strategy>> viewnumTop3(Long destId) {
        return R.ok(strategyService.findViewnumTop3ByDestId(destId));
    }

    @GetMapping("/content")
    public R<StrategyContent> getContentById(Long id) {
        return R.ok(strategyService.getContentById(id));
    }

    @GetMapping("/detail")
    public R<Strategy> detail(Long id) {
        // 阅读数+1
        strategyService.viewnumIncr(id);
        return R.ok(strategyService.getById(id));
    }

    @GetMapping("/getById")
    public Strategy getById(Long id) {
        return strategyService.getById(id);
    }

    @GetMapping("/conditions")
    public R<Map<String, List<StrategyCondition>>> getConditions() {
        Map<String, List<StrategyCondition>> map = new HashMap<>();
        // 查询国内
        List<StrategyCondition> chinaCondition = strategyService.findDestCondition(Strategy.ABROAD_NO);
        map.put("chinaCondition", chinaCondition);
        // 查询国外
        List<StrategyCondition> abroadCondition = strategyService.findDestCondition(Strategy.ABROAD_YES);
        map.put("abroadCondition", abroadCondition);
        // 查询主题条件
        List<StrategyCondition> themeCondition = strategyService.findThemeCondition();
        map.put("themeCondition", themeCondition);
        return R.ok(map);
    }

    @PostMapping("/uploadImg")
    public JSONObject uploadIm(MultipartFile upload) {
        JSONObject result = new JSONObject();
        if (upload == null) {
            result.put("uploaded", 0);
            JSONObject error = new JSONObject();
            error.put("message", "请选择要上传的文件!");
            result.put("error", error);
            return result;
        }
        // 调用阿里云 OSS 工具类进行文件上传
        // 解决文件名重复问题
        // 1. 直接使用 UUID 替换原始文件名
        // 2. 在原始文件名后面拼接时间戳
        String fileName =
                upload.getOriginalFilename().substring(0, upload.getOriginalFilename().lastIndexOf(".")) + "_" + System.currentTimeMillis();
        // 返回阿里云可访问的 url 地址
        String url = OssUtil.upload("images", fileName, upload);
        result.put("uploaded", 1);
        result.put("fileName", upload.getOriginalFilename());
        result.put("url", url);
        return result;
    }

    @PostMapping("/save")
    public R<?> save(Strategy dest) {
        strategyService.save(dest);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(Strategy dest) {
        strategyService.updateById(dest);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id) {
        strategyService.removeById(id);
        return R.ok();
    }
}
