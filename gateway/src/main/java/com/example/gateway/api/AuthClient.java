package com.example.gateway.api;

import com.example.gateway.common.R;
import com.example.gateway.dto.AuthResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/23
 * *@Version 1.0
 **/

@Component
public class AuthClient {

    private final WebClient webClient;

    public AuthClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
//                .baseUrl("http://auth-service") // 注册中心中的服务名
                .baseUrl("http://127.0.0.1:8081/v1") // 注册中心中的服务名
                .build();
    }

    /**
     * 返回结构体是这样的，所以要用R<AuthResponseDTO>去接收data数据
     * {
     *     "code": 0,
     *     "message": "success",
     *     "data": {
     *         "allow": true
     *     },
     *     "timestamp": "2025-07-24T14:32:02.2898919"
     * }
     */
    public Mono<R<AuthResponseDTO>> getPermissions(String username, String uri, String  method) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/permission")
                        .queryParam("username", username)
                        .queryParam("uri", uri)
                        .queryParam("method", method)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<R<AuthResponseDTO>>() {});
    }
}
