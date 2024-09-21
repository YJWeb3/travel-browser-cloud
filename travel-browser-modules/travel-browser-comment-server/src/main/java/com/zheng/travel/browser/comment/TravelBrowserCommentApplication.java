package com.zheng.travel.browser.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.zheng.travel.browser.core", "com.zheng.travel.browser.comment"})
@SpringBootApplication
public class TravelBrowserCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelBrowserCommentApplication.class, args);
    }
}
