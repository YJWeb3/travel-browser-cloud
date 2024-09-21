package com.zheng.travel.browser.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启用 Spring 任务调度
 */
@EnableScheduling
@MapperScan("com.zheng.travel.browser.data.mapper")
@SpringBootApplication
public class TravelBrowserDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelBrowserDataApplication.class, args);
    }
}
