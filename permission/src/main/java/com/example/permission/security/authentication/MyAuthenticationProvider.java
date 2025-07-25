package com.example.permission.security.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

//@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String credentials = (String)authentication.getCredentials();
        System.out.println("name: " + name + "\tpassword: " + credentials);
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
