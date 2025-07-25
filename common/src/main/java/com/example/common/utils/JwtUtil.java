package com.example.common.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * *@Description 使用hutool实现jwt功能
 * *@Author wuka
 * *@Date 2025/7/15
 * *@Version 1.0
 **/

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil { //后续会依赖配置中心注入secret，所以使用@Component比方法为static更合适

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.exp}")
    private Long accessExp;

    @Value("${jwt.refresh.exp}")
    private Long refreshExp;


    // 生成 JWT 令牌
    public String generateAccessToken(String username, String tenant_id) {
        // 创建 JWT 负载
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expTime = now.plusMinutes(accessExp); // 有效期
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);  // 放入需要的负载信息
        claims.put("tenant_id", tenant_id); // 租户id
//        claims.put("role", roles);  //roles
        claims.put("iat", now);  // 发布时间
        claims.put("exp", expTime);  // 过期时间
        claims.put("type", "access");

        return JWTUtil.createToken(claims, jwtSecret.getBytes());
    }

    // 生成refresh，通过refresh token来重新生成access token
    public String generateRefreshToken(String username, String tenant_id) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expTime = now.plusDays(refreshExp);
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", username);
        payload.put("tenant_id", tenant_id); // 后续基于refresh_token生成access_token时可以直接获取
        payload.put("type", "refresh");
        payload.put("iat", now);  // 发布时间
        payload.put("exp", expTime);  // 过期时间
        log.info("生成refresh token成功");
        return JWTUtil.createToken(payload, jwtSecret.getBytes());
    }

    public Boolean verifyRefreshToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            boolean verifyKey = jwt.setKey(jwtSecret.getBytes()).verify();
            if (!verifyKey) {
                log.error("签名验证失败");
                return false;
            }
            String type = (String)jwt.getPayload("type");
            if (!("refresh".equals(type))){
                log.error("token的类型不为refresh");
                return false;
            }
            JWTValidator.of(jwt).validateDate(); // 验证日期，日期超过会抛异常
        } catch (ValidateException e) {
            log.error("token超期");
            return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getUsername(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return (String)jwt.getPayload("sub");
    }

    public String getTenantID(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return (String)jwt.getPayload("tenant_id");
    }

    // 验证jwt token
    public boolean validateToken(String authToken) {
        try {
            if (!JWTUtil.verify(authToken, jwtSecret.getBytes())) { // token签名验证
                return false;
            }
            // 解析 JWT 获取负载
            JWT jwt = JWTUtil.parseToken(authToken);
            JWTValidator.of(jwt).validateDate(); // 验证日期，日期超过会抛异常

            // 获取负载信息
//            String username = (String) jwt.getPayload("sub");
//            String role = (String) jwt.getPayload("role");
//            System.out.println("Username: " + username);
//            System.out.println("Role: " + role);
            return true;

        } catch (ValidateException e) {
            log.error("token 已过期");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
//        JwtUtil jwt = new JwtUtil(properties);
////        String token = jwt.generateToken("username");
//        jwt.verifyRefreshToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3NTI2NTExNDEsImlhdCI6MTc1MjY1MTA4MSwidXNlcm5hbWUiOiJhZG1pbiJ9.CzrC00LQ_BRqYaH_INpMAECX9WeTwGH5RkIkVs3pcNM");
    }
}
