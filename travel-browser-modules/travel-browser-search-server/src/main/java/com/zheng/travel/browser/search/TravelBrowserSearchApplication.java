package com.zheng.travel.browser.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@ComponentScan(basePackages = {"com.zheng.travel.browser.core", "com.zheng.travel.browser.search"})
@SpringBootApplication
public class TravelBrowserSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelBrowserSearchApplication.class, args);
    }
}
