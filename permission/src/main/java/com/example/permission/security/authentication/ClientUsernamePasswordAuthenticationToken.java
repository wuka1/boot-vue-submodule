package com.example.permission.security.authentication;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class ClientUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String password;

    public ClientUsernamePassowordAuthenticationToken(Object principal, Object credentials, String password) {
        super(principal, credentials);
        this.password = password;
    }
}
