package com.example.auth.config;

import com.example.auth.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/15
 * *@Version 1.0
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final WhitelistProperties whitelistProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {  // spring security 5.4
        // .x版本以上不需要WebSecurityConfigurerAdapter
        // 基于 Servlet 路径做权限匹配的，不包含 context-path
        http.csrf().disable()
                .authorizeRequests().antMatchers(whitelistProperties.getUri().toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .userDetailsService(userDetailsServiceImpl);
//                .authenticationProvider(daoAuthenticationProvider()); //使用默认的认证逻辑UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 供AuthService进行auth时调用
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 认证--注入自定义的用户认证逻辑：userDetailsServiceImpl
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsServiceImpl);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }

}
