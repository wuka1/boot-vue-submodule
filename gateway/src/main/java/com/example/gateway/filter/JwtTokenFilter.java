//package com.example.gateway.filter;
//
//import com.example.gateway.util.JwtTokenProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//
///**
// * *@Description 基于webflux的filter实现jwt的截取
// * *@Author wuka
// * *@Date 2025/7/15
// * *@Version 1.0
// **/
//
//@Component
//public class JwtTokenFilter implements WebFilter {// webflux下不是OncePerRequestFilter
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        // 从请求中提取 JWT Token
//        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//        // 如果 token 不为空且以 "Bearer " 开头
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);  // 去掉 "Bearer " 前缀
//            if (jwtTokenProvider.validateToken(token)) {
//                String username = jwtTokenProvider.getUserNameFromJwt(token);
//                // 创建认证信息并设置到 SecurityContext
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        // 将请求传递到下一个过滤器
//        return chain.filter(exchange);
//    }
//}
//
