package com.zheng.travel.browser.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunConfig {

    @Value("${aliyun.accessKey.id}")
    private String accessKeyId;
    @Value("${aliyun.accessKey.secret}")
    private String accessKeySecret;
    @Value("${aliyun.dysms.endpoint}")
    private String endpoint;

    /**
     * 使用AK&SK初始化账号Client
     */
    @Bean
    public com.aliyun.dysmsapi20170525.Client smsClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = endpoint;
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
}
