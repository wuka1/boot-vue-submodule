package com.example.demoservice.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * *@Description spring cloud gateway为非阻塞式，不能使用openfeign
 * *@Author wuka
 * *@Date 2025/7/22
 * *@Version 1.0
 **/

@FeignClient(name = "auth-service", path = "/auth")
public interface AuthPermissionClient {
    @GetMapping("/permission")
    Boolean hasPermission(
            @RequestParam("userId") String userId,
            @RequestParam("uri") String uri,
            @RequestParam("method") String method
    );
}
