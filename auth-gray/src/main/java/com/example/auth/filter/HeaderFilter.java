package com.example.auth.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * *@Description 验证收到的请求头
 * *@Author wuka
 * *@Date 2025/8/1
 * *@Version 1.0
 **/

@Component
public class HeaderFilter implements Filter {

    private static final String TRAFFIC_TAG = "X-Traffic-Tag";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String trafficTag = httpRequest.getHeader(TRAFFIC_TAG);
        System.out.println("==============请求头为:" + trafficTag);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
