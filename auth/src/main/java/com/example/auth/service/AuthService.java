package com.example.auth.service;

import com.example.auth.common.ErrorInfo;
import com.example.auth.dto.LoginRequestDTO;
import com.example.auth.dto.RefreshRequestDTO;
import com.example.auth.dto.RefreshTokenResponseDTO;
import com.example.auth.dto.TokenResponseDTO;
import com.example.auth.exception.AuthException;
import com.example.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/17
 * *@Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;



    public TokenResponseDTO login(LoginRequestDTO request) {
        // 请求封装--默认返回UsernamePasswordAuthenticationToken--可自定义
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        //进行认证--多个AuthenticationProvider依次厂商
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth); // 作用？
        // 认证通过，生成jwt-token
        String accessToken = jwtUtil.generateAccessToken(auth.getPrincipal().toString(), request.getTenantID());
        String refreshToken = jwtUtil.generateRefreshToken(auth.getPrincipal().toString(), request.getTenantID());
        return new TokenResponseDTO(accessToken, refreshToken);
    }

    public RefreshTokenResponseDTO refresh(RefreshRequestDTO token) {
        //
        if (!(jwtUtil.verifyRefreshToken(token.getRefreshToken()))) {
            throw new AuthException(ErrorInfo.VALID_TOKEN_USE.getCode(), "token验证失败"); // code 统一管理更优雅
        }
        String username = jwtUtil.getUsername(token.getRefreshToken());
        String tenant_id = jwtUtil.getTenantID(token.getRefreshToken());
        String newAccessToken = jwtUtil.generateAccessToken(username, tenant_id);
        return new RefreshTokenResponseDTO(newAccessToken);
    }
}
