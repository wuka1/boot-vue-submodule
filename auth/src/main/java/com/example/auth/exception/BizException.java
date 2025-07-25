package com.example.auth.exception;

import lombok.Getter;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/16
 * *@Version 1.0
 **/
@Getter
public class BizException extends RuntimeException{
    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

}
