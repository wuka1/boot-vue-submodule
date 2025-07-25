package com.example.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/17
 * *@Version 1.0
 **/


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseVO {

    private String refreshToken;
    
}
