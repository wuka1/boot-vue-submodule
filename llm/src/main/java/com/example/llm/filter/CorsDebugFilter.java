package com.example.llm.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/8/12
 * *@Version 1.0
 **/
@Component
public class CorsDebugFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("=== 请求头 ===");
        exchange.getRequest().getHeaders().forEach((k, v) -> System.out.println(k + ": " + v));

        return chain.filter(exchange).doOnTerminate(() -> {
            System.out.println("=== 响应头 ===");
            exchange.getResponse().getHeaders().forEach((k, v) -> System.out.println(k + ": " + v));
        });
    }
}