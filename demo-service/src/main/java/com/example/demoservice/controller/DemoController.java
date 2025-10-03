package com.example.demoservice.controller;

import com.example.common.result.R;
import com.example.demoservice.annotation.AuthRequire;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 业务服务案例
 **/
@RestController
@RequestMapping("/v1/demo")
@Slf4j
public class DemoController {
    /**
     * 带有鉴权的接口验证
     * @return 成功或失败标识
     */
    @AuthRequire(resource = "/api/test", action = "Get")
    @GetMapping("/order")
    public R<String> getOrder() {
        // 自动从路径变量获取orderId作为资源ID
        log.info("鉴权成功");
        return R.success("鉴权成功");
    }

    /**
     * request param demo
     * @param userId 用户id
     * @return 返回体
     */

    @GetMapping("/user")
    public R<String> getUser(@RequestParam String userId) {
        // 自动从路径变量获取orderId作为资源ID
        log.info("请求参数为:{}", userId);
        return R.success(userId);
    }

    /**
     * request pathvariable 测试
     * @param userId 用户id
     * @return 返回体
     */
    @GetMapping("/user/{userId}")
    public R<String> getUserInfo(@PathVariable String userId) {
        // 自动从路径变量获取orderId作为资源ID
        log.info("请求参数为:{}", userId);
        return R.success(userId);
    }
}
