package com.zheng.travel.browser.search.controller;

import com.zheng.travel.browser.article.dto.DestinationDto;
import com.zheng.travel.browser.article.dto.StrategyDto;
import com.zheng.travel.browser.article.dto.TravelDto;
import com.zheng.travel.browser.core.utils.R;
import com.zheng.travel.browser.search.domain.DestinationEs;
import com.zheng.travel.browser.search.domain.StrategyEs;
import com.zheng.travel.browser.search.domain.TravelEs;
import com.zheng.travel.browser.search.domain.UserInfoEs;
import com.zheng.travel.browser.search.feign.ArticleFeignService;
import com.zheng.travel.browser.search.feign.UserInfoFeignService;
import com.zheng.travel.browser.search.qo.SearchQueryObject;
import com.zheng.travel.browser.search.service.ElasticsearchService;
import com.zheng.travel.browser.search.vo.SearchResult;
import com.zheng.travel.browser.user.dto.UserInfoDTO;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/q")
public class SearchController {

    private final ElasticsearchService elasticsearchService;
    private final ArticleFeignService articleFeignService;
    private final UserInfoFeignService userInfoFeignService;

    public SearchController(ArticleFeignService articleFeignService, UserInfoFeignService userInfoFeignService, ElasticsearchService elasticsearchService) {
        this.articleFeignService = articleFeignService;
        this.userInfoFeignService = userInfoFeignService;
        this.elasticsearchService = elasticsearchService;
    }

    @GetMapping
    public R<?> search(SearchQueryObject qo) {
        try {
            // keyword 参数解码
            qo.setKeyword(URLDecoder.decode(qo.getKeyword(), "UTF-8"));

            log.info("[搜索服务] 开始搜索 query={}", JSON.toJSONString(qo));

            // 请求分发器
            switch (qo.getType()) {
                case 0:
                    // 目的地
                    return this.searchForDest(qo);
                case 1:
                    // 攻略
                    return this.searchForStrategy(qo);
                case 2:
                    // 游记
                    return this.searchForTravel(qo);
                case 3:
                    // 用户
                    return this.searchForUserInfo(qo);
                default:
                    // 所有
                    return this.searchForAll(qo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    private R<?> searchForAll(SearchQueryObject qo) {
        SearchResult result = new SearchResult();
        Page<UserInfoDTO> page = elasticsearchService.searchWithHighlight(
                UserInfoEs.class,
                UserInfoDTO.class,
                qo,
                (clazz, id) -> userInfoFeignService.getById(id).checkAndGet(),
                "city", "info"
        );
        result.setUsers(page.getContent());
        result.setTotal(page.getTotalElements());

        Page<TravelDto> travelPage = elasticsearchService.searchWithHighlight(
                TravelEs.class,
                TravelDto.class,
                qo,
                (clazz, id) -> articleFeignService.getTravelById(id).checkAndGet(),
                "title", "summary"
        );
        result.setTravels(travelPage.getContent());
        result.setTotal(result.getTotal() + travelPage.getTotalElements());

        Page<StrategyDto> strategyPage = elasticsearchService.searchWithHighlight(
                StrategyEs.class,
                StrategyDto.class,
                qo,
                (clazz, id) -> articleFeignService.getStrategyById(id),
                "title", "subTitle", "summary"
        );
        result.setStrategies(strategyPage.getContent());
        result.setTotal(result.getTotal() + strategyPage.getTotalElements());

        Page<DestinationDto> destPage = elasticsearchService.searchWithHighlight(
                DestinationEs.class,
                DestinationDto.class,
                qo,
                (clazz, id) -> articleFeignService.getDestById(id).checkAndGet(),
                "name", "info"
        );
        result.setDests(destPage.getContent());
        result.setTotal(result.getTotal() + destPage.getTotalElements());

        JSONObject json = new JSONObject();
        json.put("result", result);
        json.put("qo", qo);
        return R.ok(json);
    }

    private R<?> searchForUserInfo(SearchQueryObject qo) {
        Page<UserInfoDTO> page = elasticsearchService.searchWithHighlight(
                UserInfoEs.class,
                UserInfoDTO.class,
                qo,
                (clazz, id) -> userInfoFeignService.getById(id).checkAndGet(),
                "city", "info"
        );

        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("qo", qo);
        return R.ok(json);
    }

    private R<?> searchForTravel(SearchQueryObject qo) {
        Page<TravelDto> page = elasticsearchService.searchWithHighlight(
                TravelEs.class,
                TravelDto.class,
                qo,
                (clazz, id) -> articleFeignService.getTravelById(id).checkAndGet(),
                "title", "summary"
        );

        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("qo", qo);
        return R.ok(json);
    }

    private R<?> searchForStrategy(SearchQueryObject qo) {
        Page<StrategyDto> page = elasticsearchService.searchWithHighlight(
                StrategyEs.class,
                StrategyDto.class,
                qo,
                (clazz, id) -> articleFeignService.getStrategyById(id),
                "title", "subTitle", "summary"
        );

        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("qo", qo);
        return R.ok(json);
    }

    private R<?> searchForDest(SearchQueryObject qo) {
        SearchResult result = new SearchResult();
        // 1. 远程调用目的地模块根据名称查询目的地
        R<DestinationDto> destResult = articleFeignService.getDestByName(qo.getKeyword());
        DestinationDto dest = destResult.checkAndGet();
        // 2. 判断目的地对象是否存在, 如果不存在, 直接返回空对象
        if (dest != null) {
            result.setTotal(1L);

            // 3. 远程调用攻略接口, 基于目的地名称查询攻略列表
            R<List<StrategyDto>> strategyResult = articleFeignService.findStrategyByDestName(qo.getKeyword());
            List<StrategyDto> strategyDtoList = strategyResult.checkAndGet();
            result.setStrategies(strategyDtoList);
            result.setTotal(result.getTotal() + strategyDtoList.size());

            // 4. 远程调用游记接口, 基于目的地名称查询游记列表
            R<List<TravelDto>> travelResult = articleFeignService.findTravelByDestName(qo.getKeyword());
            List<TravelDto> travelDtoList = travelResult.checkAndGet();
            result.setTravels(travelDtoList);
            result.setTotal(result.getTotal() + travelDtoList.size());

            // 5. 远程调用用户接口, 基于目的地名称查询用户列表
            R<List<UserInfoDTO>> userResult = userInfoFeignService.findUserByDestName(qo.getKeyword());
            List<UserInfoDTO> userInfoDTOList = userResult.checkAndGet();
            result.setUsers(userInfoDTOList);
            result.setTotal(result.getTotal() + userInfoDTOList.size());
        }
        // 6. 封装结果对象并返回
        JSONObject json = new JSONObject();
        json.put("qo", qo);
        json.put("result", result);
        json.put("dest", dest);
        return R.ok(json);
    }
}
