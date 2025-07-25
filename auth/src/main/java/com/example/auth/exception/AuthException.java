package com.example.common.utils.exception;

import lombok.Getter;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/17
 * *@Version 1.0
 **/

@Getter
public class AuthException extends RuntimeException{

    private final int code;

    public AuthException(int code, String message) {
        super(message);
        this.code = code;
    }
}
