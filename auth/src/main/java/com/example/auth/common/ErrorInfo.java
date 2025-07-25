package com.example.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/16
 * *@Version 1.0
 **/
@Getter
@AllArgsConstructor
public enum ErrorInfo {

    SUCCESS(0, "成功"),

    // 基础错误
    VALID_METHOD_ARG(100100001, "校验请求体错误"),
    VALID_REQUEST_PARAM(100100002, "校验请求参数错误"),
    VALID_JSON(100100003, "校验JSON错误"),
    // 业务模块异常码:系统编码3位-业务编码3位-异常编码3位
    // 用户模块100
    USER_NOT_FOUND(100200001, "用户不存在"),
    // 权限模块
    USER_NOT_PERMISSION(100300001, "用户权限不足"),
    VALID_TOKEN_USE(100300002, "token无效"),
    VALID_TOKEN_EXP(100300002, "token超期");


    private final int code;
    private final String message;
}
