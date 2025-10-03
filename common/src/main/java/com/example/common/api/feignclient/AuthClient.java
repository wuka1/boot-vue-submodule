package com.example.common.api.feignclient;

import com.example.common.api.dto.AuthResponseDTO;
import com.example.common.api.fallbackfactory.AuthClientFallbackFactory;
import com.example.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * *@Description spring cloud gateway为非阻塞式，不能使用openfeign
 * *@Author wuka
 * *@Date 2025/7/22
 * *@Version 1.0
 **/

@FeignClient(name = "${auth.service.url}", path = "v1", fallbackFactory = AuthClientFallbackFactory.class) // auth-service为注册中心注册名
//@FeignClient(name = "authClient", url = "${auth.service.url}") // url本地
public interface AuthClient {
    @GetMapping("/auth/check")
    R<AuthResponseDTO> hasPermission(@RequestParam("username") String username,
                                     @RequestParam("uri") String uri,
                                     @RequestParam("method") String method);
}
