package com.example.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/23
 * *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private LocalDateTime timestamp;

    public static <T> R<T> success(T data) {
        return new R<>(0, "success", data, LocalDateTime.now());
    }

    // 指定code和message
    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null, LocalDateTime.now());
    }

    // 适配错误信息结构体
    public static <T> R<T> fail(ErrorInfo error) {
        return fail(error.getCode(), error.getMessage());
    }

    // 默认失败
    public static <T> R<T> fail(String message) {
        return fail(500, message);
    }

    // 支持链式调用设置 data
    public R<T> setData(T data) {
        this.data = data;
        return this;
    }
}

