package com.example.inteceptor;

import com.example.service.IpCountService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 实现自定义功能拦截
 */

public class IPCounterInterceptor implements HandlerInterceptor {

    @Resource
    private IpCountService ipCountService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ipCountService.record();
        return true;
    }
}
