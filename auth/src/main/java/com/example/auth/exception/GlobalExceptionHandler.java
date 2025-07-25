package com.example.common.utils.exception;

import com.example.auth.common.ErrorInfo;
import com.example.auth.common.R;
import com.example.common.utils.exception.AuthException;
import com.example.common.utils.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/16
 * *@Version 1.0
 **/

@RestControllerAdvice
@Slf4j  //lombok简化LoggerFactory.getLogger(YourClass.class)操作
public class GlobalExceptionHandler {

     // 通用业务异常处理
    @ExceptionHandler(BizException.class)
    public R<?> handleBizException(BizException ex) {
        log.warn("业务异常：{}", ex.getMessage());
        return R.fail(ex.getCode(), ex.getMessage());
    }

    // 参数校验失败（如 @Valid, @Validated）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("参数校验失败: {}", ex.getMessage());
        return R.fail(ErrorInfo.VALID_METHOD_ARG.getCode(), message);
    }

    // 单个字段校验异常（如 @RequestParam 校验）
    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("单个字段校验异常: {}", ex.getMessage());
        return R.fail(ErrorInfo.VALID_REQUEST_PARAM.getCode(), ex.getMessage());
    }

    // 请求体 JSON 解析异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> handleJsonParseError(HttpMessageNotReadableException ex) {
        log.error("请求体JSON解析异常: {}", ex.getMessage());
        return R.fail(ErrorInfo.VALID_JSON.getCode(), "请求体格式错误");
    }

    // 通用业务异常处理
    @ExceptionHandler(AuthException.class)
    public R<?> handleAuthException(AuthException ex) {
        log.warn("认证业务异常：{}", ex.getMessage());
        return R.fail(ex.getCode(), ex.getMessage());
    }

    // 兜底异常
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception ex) {
        log.error("系统异常", ex);
        return R.fail(500, "服务器内部错误");
    }
}
