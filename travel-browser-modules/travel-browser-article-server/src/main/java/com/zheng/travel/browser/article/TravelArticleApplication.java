package com.zheng.travel.browser.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.zheng.travel.browser.core", "com.zheng.travel.browser.article"})
@EnableFeignClients
@SpringBootApplication
public class TravelArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelArticleApplication.class, args);
    }
}
