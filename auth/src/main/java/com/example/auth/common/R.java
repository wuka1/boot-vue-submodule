package com.example.common.utils.result;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/16
 * *@Version 1.0
 **/
import com.example.common.utils.exception.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class R<T> {

    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> R<T> success(T data) {
        return new R<>(0, "success", data, LocalDateTime.now());
    }

    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null, LocalDateTime.now());
    }

    public static <T> R<T> fail(ErrorInfo error) {
        return fail(error.getCode(), error.getMessage());
    }
}

