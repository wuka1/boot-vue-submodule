package com.example.auth.controller;

import com.example.auth.common.R;
import com.example.auth.dto.*;
import com.example.auth.service.AuthService;
import com.example.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/15
 * *@Version 1.0
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {

     // 使用Lombok的RequiredArgsConstructor还行构造器实现
    /**
     * 认证服务注入和构造
     */
    private final AuthService authService;
    /**
     * 权限服务注入和构造
     */
    private final PermissionService permissionService;

//    @Autowired
//    public AuthController(JwtUtils jwtUtils) { // @RequiredArgsConstructor可以替代该方法
//        this.jwtUtils = jwtUtils;
//    }

      // 字段注入--存在未初始化就被使用的场景
//    @Resource // 自动注入
//    private JwtUtils jwtUtils;


    @PostMapping("/login")
    public R<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // 登录认证
        return R.success(authService.login(request));
    }

    @PostMapping("/refresh")
    public R<RefreshTokenResponseDTO> refresh(@Valid @RequestBody RefreshRequestDTO token) {
        // refresh token
        return R.success(authService.refresh(token));
    }

    /**
     * api认证--前端根据权限动态加载菜单或按钮
     *
     * @param permission 鉴权请求体
     * @return 鉴权结果
     */
    @GetMapping("/check") // @ModelAttribute可省略
    public R<PermissionResponseDTO> hasPermission(@Valid PermissionRequestDTO permission) {
        // api 权限认证
        return R.success(permissionService.hasPermission(permission));
    }
}
