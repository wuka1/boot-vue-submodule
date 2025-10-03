package com.example.demoservice.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/29
 * *@Version 1.0
 **/

@Configuration
public class ConfigManager {

    /**
     * feign 重试配置
     */
    @Bean
    public Retryer feignRetryer() {
        // period: 初始间隔，maxPeriod：最大间隔，maxAttempts：最大尝试次数
        return new Retryer.Default(1000, 5000, 3);
    }

}
