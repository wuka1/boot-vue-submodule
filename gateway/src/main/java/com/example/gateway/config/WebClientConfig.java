package com.example.gateway.api;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/23
 * *@Version 1.0
 **/

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced  // 支持服务名调用（如 http://auth-service）
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
