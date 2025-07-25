package com.example.permission.security.authentication;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

//@Data
public class ClientUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * 放入额外的参数
     */
    private final String password;

    public ClientUsernamePasswordAuthenticationToken(Object principal, Object credentials, String password) {
        super(principal, credentials);
        this.password = password;
    }
}
