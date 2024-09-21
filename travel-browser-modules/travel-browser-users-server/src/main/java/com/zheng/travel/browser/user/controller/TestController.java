package com.zheng.travel.browser.user.controller;

import com.zheng.travel.browser.auth.anno.RequireLogin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequireLogin
@RestController
@RequestMapping("/test1")
public class TestController {

    @GetMapping("/index")
    public String test1() {
        return "test1:index";
    }

    @GetMapping("/detail")
    public String detail() {
        return "test1:detail";
    }
}
