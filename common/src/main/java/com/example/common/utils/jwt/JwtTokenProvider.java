package com.example.common.utils.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * *@Description 使用jjwt实现jwt功能--当前使用hutool-jwt来实现
 * *@Author wuka
 * *@Date 2025/7/15
 * *@Version 1.0
 **/
@Component
public class JwtTokenProvider {
    private final String jwtSecret = "g8F2lAdFz4J7HqgqYfGG9k2WyHlFmjyd0bNvYYp/jXA=";

    // 生成 JWT 令牌
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86400000)) // 24小时有效期
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // 从 JWT 中提取用户名
    public String getUserNameFromJwt(String token) {
        return Jwts.parser()  // 使用 parserBuilder() 而非 parser()
                .setSigningKey(jwtSecret)  // 设置密钥
                .build()
                .parseClaimsJws(token)    // 解析 JWT
                .getBody()
                .getSubject();
    }

    // 校验 JWT 是否有效
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()  // 使用 parserBuilder()
                    .setSigningKey(jwtSecret)  // 设置密钥
                    .build()
                    .parseClaimsJws(authToken);  // 解析 JWT
            return true;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT Token is invalid", e);
        }
    }
}
