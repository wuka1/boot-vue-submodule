package com.example.demoservice.result;

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
@AllArgsConstructor
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private LocalDateTime timestamp;
}

