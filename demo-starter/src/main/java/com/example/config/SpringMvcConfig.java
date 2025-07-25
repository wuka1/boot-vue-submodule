package com.example.config;

import com.example.inteceptor.IPCounterInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注入spring环境
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipCounterInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public IPCounterInterceptor ipCounterInterceptor(){
        return new IPCounterInterceptor();
    }
}
