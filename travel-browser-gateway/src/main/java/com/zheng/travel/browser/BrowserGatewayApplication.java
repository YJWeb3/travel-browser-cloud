package com.zheng.travel.browser;

import com.zheng.travel.browser.core.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(excludeFilters =
        {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SwaggerConfig.class)})
public class BrowserGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrowserGatewayApplication.class,args);
    }
}