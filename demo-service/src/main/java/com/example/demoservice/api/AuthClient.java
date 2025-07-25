package com.example.demoservice.api;

import com.example.demoservice.dto.AuthRequestDTO;
import com.example.demoservice.dto.AuthResponseDTO;
import com.example.demoservice.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * *@Description spring cloud gateway为非阻塞式，不能使用openfeign
 * *@Author wuka
 * *@Date 2025/7/22
 * *@Version 1.0
 **/

//@FeignClient(name = "authClient", path = "${auth.service.url}") //path-注册中心
@FeignClient(name = "authClient", url = "${auth.service.url}") // url 本地
public interface AuthClient {
    @GetMapping("/auth/check")
    R<AuthResponseDTO> hasPermission(@RequestParam("username") String username,
                    @RequestParam("uri") String uri,
                    @RequestParam("method") String method);
}
