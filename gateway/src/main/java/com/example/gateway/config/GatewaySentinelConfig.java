package com.example.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/25
 * *@Version 1.0
 **/

@Configuration
@Slf4j
public class GatewaySentinelConfig {

    @PostConstruct
    public void initGatewayRules() {
        log.info("加载 Gateway 限流规则");
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule("demo_route") // routeId--gateway对应的id
                .setCount(1) // 每秒允许通过的请求数
                .setIntervalSec(1)); // 统计时间窗口
        GatewayRuleManager.loadRules(rules);
        log.info("限流规则加载完成");
    }

    // Sentinel Block 限流拦截响应处理
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelBlockHandler() {
        return (exchange, chain) -> chain.filter(exchange).onErrorResume(throwable -> {
            if (BlockException.isBlockException(throwable)) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                String body = "{\"code\":429,\"message\":\"请求被 Sentinel 限流了\"}";
                DataBuffer buffer = response.bufferFactory()
                        .wrap(body.getBytes(StandardCharsets.UTF_8));
                log.error("请求受限,{}",body);
                return response.writeWith(Mono.just(buffer));
            }
            return Mono.error(throwable);
        });
    }

}
