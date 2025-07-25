package com.example.demoservice.dto;

import lombok.Data;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/24
 * *@Version 1.0
 **/

@Data
public class AuthRequestDTO {

    private String username;
    private String uri;
    private String method;

}
