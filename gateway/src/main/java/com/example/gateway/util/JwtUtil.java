//package com.example.gateway.util;
//
//import cn.hutool.core.exceptions.ValidateException;
//import cn.hutool.jwt.JWT;
//import cn.hutool.jwt.JWTUtil;
//import cn.hutool.jwt.JWTValidator;
//import org.springframework.stereotype.Component;
//
///**
// * *@Description 使用hutool的jwtutil封装
// * *@Author wuka
// * *@Date 2025/7/15
// * *@Version 1.0
// **/
//@Component
//public class JwtUtil {
//
//    // 这个secret_key要和auth的一样，所以要把这个值放置到配置中心
//    private final String SECRET_KEY = "gateway-auth";
//
//    // 验证jwt token
//    public boolean validateToken(String authToken) {
//        try {
//            if (!JWTUtil.verify(authToken, SECRET_KEY.getBytes())) { // token签名验证
//                return false;
//            }
//            // 解析 JWT 获取负载
//            JWT jwt = JWTUtil.parseToken(authToken);
//            JWTValidator.of(jwt).validateDate(); // 验证日期，日期超过会抛异常
//
//            // 获取负载信息
//            String username = (String) jwt.getPayload("username");
////            String role = (String) jwt.getPayload("role");
//
//            System.out.println("Username: " + username);
////            System.out.println("Role: " + role);
//            return true;
//
//        } catch (ValidateException e) {
//            System.out.println("token 已过期");
//            return false;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // refresh jwt token
//
//
//}