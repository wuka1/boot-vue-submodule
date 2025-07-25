package com.example.auth.dto;

import lombok.Data;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/22
 * *@Version 1.0
 **/

@Data
public class PermissionRequestDTO {

    private String username;
    private String uri;
    private String method;
}
