package com.example.gateway.filter;

import com.example.common.utils.JwtUtil;
import com.example.gateway.config.WhitelistProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * *@Description 基于gateway的GlobalFilter截取jwt
 * *@Author wuka
 * *@Date 2025/7/14
 * *@Version 1.0
 **/

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
//    private final AuthClient authClient; //直接调用auth服务
    private final WhitelistProperties whitelistProperties;

    // 路径匹配
    private static final AntPathMatcher pathMatcher = new AntPathMatcher(); // contains不能处理/auth/**等匹配

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("执行了自定义的全局过滤器");
        // 1.过滤特殊URL，直接跳转到对应服务-一般为认证服务
        ServerHttpRequest request = exchange.getRequest();

        // 跳过登录和公开端点
        String path = request.getURI().getPath(); //原始请求路径（未经 Gateway 修改）
//        request.getPath();   // 经过gateway处理后的路径
        // 判断是否在白名单中
        for (String pattern : whitelistProperties.getUri()) {
            if (pathMatcher.match(pattern, path)) {
                return chain.filter(exchange); // 白名单路径直接放行
            }
        }

        //2.获取请求参数access-token
        String token = request.getHeaders().getFirst("Authorization");
        //3.1 判断是否存在
        if (token == null || !token.startsWith("Bearer ")) {
            // 如果不存在 : 认证失败
            log.warn("token验证失败，token为:" + token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); //请求结束
        }
        //3.2 如果存在,验证jwt
        token = token.substring(7);  // 去掉 "Bearer " 前缀
        if (!jwtUtil.validateToken(token)) { // 如果jwt无效-过期、篡改等
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); //返回401
            return exchange.getResponse().setComplete(); //请求结束
        }
        //4. 转发令牌到路由后的服务
        return chain.filter(addInfoToRequest(exchange, token)); //继续向下执行
        //方案2: 调用auth服务进行RBAC认证--不合理放置到业务侧调用比较合理
//        String username = jwtUtil.getUsername(token);
//        String uri = request.getURI().getPath();
//        String method = request.getMethodValue();
////        // 通过webclient接口调用AuthService
//        return authClient.getPermissions(username, uri, method)
//                .map(R::getData)
//                .flatMap(
//                        response -> {
//                            if (response != null && response.isAllow()) {
//                                return chain.filter(exchange);
//                            } else {
//                                log.warn("权限不足");
//                                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN); //返回403;
//                                return exchange.getResponse().setComplete(); //请求结束
//                            }
//                        })
//                .onErrorResume(ex -> {
//                    // 鉴权失败，返回 401 或 500
//                    log.error("鉴权服务失败", ex);
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return exchange.getResponse().setComplete();
//                });
    }
    // 将原始认证信息转发到路由的服务
    private ServerWebExchange addInfoToRequest(ServerWebExchange exchange, String token) {
        // 重建请求并添加一些header信息
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // 不建议直接专递原始的token信息到后端服务
                .header("X-User-Id", jwtUtil.getUsername(token))
                .header("X-Tenant-Id", jwtUtil.getTenantID(token))  // 例如灰度发布信息
                .build();

        return exchange.mutate().request(newRequest).build();
    }
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

public static class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
}
