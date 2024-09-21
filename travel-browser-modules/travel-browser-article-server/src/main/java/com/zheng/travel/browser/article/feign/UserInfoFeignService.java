package com.zheng.travel.browser.article.feign;

import com.zheng.travel.browser.core.utils.R;
import com.zheng.travel.browser.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("travel-browser-user-service")
public interface UserInfoFeignService {

    @GetMapping("/users/getById")
    R<UserInfoDTO> getById(@RequestParam Long id);

    @GetMapping("/users/favor/strategies")
    R<List<Long>> getFavorStrategyIdList(@RequestParam Long userId);
}
