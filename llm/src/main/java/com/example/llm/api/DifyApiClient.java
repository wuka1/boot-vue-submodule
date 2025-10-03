package com.example.llm.api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/8/12
 * *@Version 1.0
 **/

@Component
public class DifyApiClient {

    private final WebClient webClient;

    public DifyApiClient(WebClient.Builder builder,
                         @Value("${dify.api-url}") String difyApiUrl,
                         @Value("${dify.api-key}") String difyApiKey
                         ) {
        this.webClient = builder
                .baseUrl(difyApiUrl)
                .defaultHeader("Authorization", "Bearer " + difyApiKey)
                .build();
    }

    /**
     * 发送 POST 请求，返回 SSE 流
     */
    public <T> Flux<String> postStream(String path, T body) {
        return webClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(String.class);
    }

    /**
     * 发送 POST 请求，返回普通对象
     */
    public <T, R> Mono<R> post(String path, T body, Class<R> responseType) {
        return webClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * 发送 GET 请求
     */
    public <R> Mono<R> get(String path, Class<R> responseType) {
        return webClient.get()
                .uri(path)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }
}
