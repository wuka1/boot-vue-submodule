package com.example.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/17
 * *@Version 1.0
 **/

@Data
public class RefreshRequest {
    @JsonProperty("refresh_token")  //request字段与dto不同时
    private String refreshToken;
}
