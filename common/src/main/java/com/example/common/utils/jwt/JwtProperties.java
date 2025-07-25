package com.example.common.utils.jwt;

import lombok.Data;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/21
 * *@Version 1.0
 **/

@Data
public class JwtProperties {

    private String secretKey;  //
    private long expireSeconds; // 单位：秒
    private String issuer = "auth-service";
}
