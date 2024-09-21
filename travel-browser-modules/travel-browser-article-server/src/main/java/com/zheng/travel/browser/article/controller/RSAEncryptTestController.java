//package com.zheng.travel.browser.article.controller;
//
//import com.zheng.travel.browser.core.utils.R;
//import com.zheng.travel.browser.core.utils.RsaUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/rsa/encrypt")
//public class RSAEncryptTestController {
//
//    @Value("${security.rsa.public-key}")
//    public String publicKey;
//
//    @GetMapping("/test")
//    public R<?> test() {
//        try {
//            String s = "天王盖地虎";
//            // 响应数据前进行加密
//            System.out.println("加密前: " + s);
//            String encryptData = RsaUtils.encryptByPublicKey(publicKey, s);
//            System.out.println("加密后: " + encryptData);
//            return R.ok(encryptData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return R.error(500, "加密数据失败");
//    }
//}
