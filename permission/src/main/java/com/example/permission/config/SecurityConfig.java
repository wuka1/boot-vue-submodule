package com.example.permission.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Resource
//    private MyAuthenticationProvider myAuthenticationProvider;
//
//    protected void config(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.authenticationProvider(myAuthenticationProvider);
//    }


    @Bean
    protected UserDetailsService userDetailsService() {
        //使用内存数据进行验证
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        //创建用户
        UserDetails user1 = User.withUsername("456").password("123").authorities("admin").build();
        manager.createUser(user1);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        return http
//                // 需要认证的资源
//                .authorizeHttpRequests()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login().and().build();
//
//    }
//    FilterInvocationSecurityMetadataSource

}
