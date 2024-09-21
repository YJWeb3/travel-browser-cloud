package com.zheng.travel.browser.user.feign;

import com.zheng.travel.browser.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 */
@FeignClient("travel-browser-article-service")
public interface ArticleFeignService {

    @GetMapping("/rsa/encrypt/test")
    R<String> getEncryptData();
}
