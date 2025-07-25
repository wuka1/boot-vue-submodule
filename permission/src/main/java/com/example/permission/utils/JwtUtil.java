package com.example.permission.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    public final static String secret = "gateway-auth";

    public static String createToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newTime = now.plusMinutes(10);
//        Map<String,Object> payload = new HashMap<String,Object>();
//        //签发时间
//        payload.put(JWTPayload.ISSUED_AT, now);
//        //过期时间
//        payload.put(JWTPayload.EXPIRES_AT, newTime);
//        //生效时间
//        payload.put(JWTPayload.NOT_BEFORE, now);
//        //载荷
//        payload.put("userName", "zhangsan");
//        payload.put("passWord", "666889");
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);  // 放入需要的负载信息
        payload.put("iat", now);  // 发布时间
        payload.put("exp", newTime);
        System.out.println(newTime);
        return JWTUtil.createToken(payload, secret.getBytes());
    }

    public static Boolean jwtVerify(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        boolean verifyKey = jwt.setKey(secret.getBytes()).verify();
        System.out.println(verifyKey);
        boolean a = jwt.getPayload("exp") != null; // 超期字段不为空
        boolean b = jwt.validate(0);  //是否过期
        System.out.println("a:" +a + ",b:" + b);
        System.out.println(a||b);
        if (jwt.getPayload("exp") != null && jwt.validate(0)) {
            System.out.println("Token has expired!");
            return false;
        }
//        boolean verifyTime = jwt.validate(0);
//        System.out.println(verifyTime);
        return verifyKey;
    }

    public static void main(String[] args) {
//        String token = JwtUtil.createToken("username");
        JwtUtil.jwtVerify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3NTI2MjkxNjYsImlhdCI6MTc1MjYyOTEwNiwidXNlcm5hbWUiOiJhZG1pbiJ9.d52iZ7vEQmTIYKDUs6xI3PsTodAwK_s4j1-fxHpTUsU");

    }

}
