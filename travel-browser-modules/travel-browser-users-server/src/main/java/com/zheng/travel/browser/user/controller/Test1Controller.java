package com.zheng.travel.browser.user.controller;

import com.zheng.travel.browser.auth.anno.RequireLogin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test2")
public class Test1Controller {

    private static final Map<String, Object> map = new HashMap<>();

    @GetMapping("/safijasofuhwljaisodfajoiushfvoq2iu1rd19078y/init")
    public ResponseEntity<String> init() {
        if (map.get("init") != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        map.put("init", true);
        return ResponseEntity.ok("init success");
    }

    @GetMapping("/index")
    public String test1() {
        return "test2:index";
    }

    @RequireLogin
    @GetMapping("/detail")
    public String detail() {
        return "test2:detail";
    }
}
