package com.example.auth.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/21
 * *@Version 1.0
 **/
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;    // 手机号
    private Object credentials;  // 验证码

//    private String name; // 自定义属性

    public CustomAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public CustomAuthenticationToken(Object principal,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = null;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
