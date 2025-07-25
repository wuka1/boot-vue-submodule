package com.example.demoservice.controller;

import com.example.demoservice.annotation.AuthRequire;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/24
 * *@Version 1.0
 **/

@RestController
@RequestMapping("/v1/demo")
@Slf4j
public class DemoController {
    // 带资源ID的操作
    @AuthRequire(resource = "/api/test", action = "Get")
    @GetMapping("/order")
    public String getOrder() {
        // 自动从路径变量获取orderId作为资源ID
        log.info("鉴权成功");
        return "success";
    }

    @GetMapping("/user")
    public String getUser(@RequestParam String userId) {
        // 自动从路径变量获取orderId作为资源ID
        log.info("请求参数为:{}", userId);
        return userId;
    }

    @GetMapping("/user/{userId}")
    public String getUserInfo(@PathVariable String userId) {
        // 自动从路径变量获取orderId作为资源ID
        log.info("请求参数为:{}", userId);
        return userId;
    }
}
