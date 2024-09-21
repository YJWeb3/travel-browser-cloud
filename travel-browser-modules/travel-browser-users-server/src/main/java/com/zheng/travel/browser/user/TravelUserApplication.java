package com.zheng.travel.browser.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@MapperScan("com.zheng.travel.browser.user.mapper")
@ComponentScan(basePackages = {"com.zheng.travel.browser.core", "com.zheng.travel.browser.user"})
@SpringBootApplication
public class TravelUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelUserApplication.class, args);
    }
}
