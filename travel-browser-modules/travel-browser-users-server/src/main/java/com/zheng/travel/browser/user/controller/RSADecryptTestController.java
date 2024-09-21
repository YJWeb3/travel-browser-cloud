package com.zheng.travel.browser.user.controller;

import com.zheng.travel.browser.core.utils.R;
import com.zheng.travel.browser.core.utils.RsaUtils;
import com.zheng.travel.browser.user.feign.ArticleFeignService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rsa/decrypt")
public class RSADecryptTestController {

    @Value("${security.rsa.private-key}")
    private String privateKey;

    private final ArticleFeignService articleFeignService;

    public RSADecryptTestController(ArticleFeignService articleFeignService) {
        this.articleFeignService = articleFeignService;
    }

    @GetMapping("/test")
    public R<?> test() {
        try {
            R<String> result = articleFeignService.getEncryptData();
            String encryptData = result.checkAndGet();
            System.out.println("解密前: " + encryptData);
            String decryptData = RsaUtils.decryptByPrivateKey(privateKey, encryptData);
            System.out.println("解密后: " + decryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }
}
