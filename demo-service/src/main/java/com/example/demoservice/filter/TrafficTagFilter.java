//package com.example.demoservice.filter;
//
//import com.example.demoservice.utils.TrafficTagHolder;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * *@Description TODO
// * *@Author wuka
// * *@Date 2025/8/1
// * *@Version 1.0
// **/
//@Component
//public class TrafficTagFilter implements Filter {
//
//    private static final String TRAFFIC_TAG = "X-Traffic-Tag";
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        try {
//            HttpServletRequest httpRequest = (HttpServletRequest) request;
//            String trafficTag = httpRequest.getHeader(TRAFFIC_TAG);
//            if (trafficTag != null && !trafficTag.isEmpty()) {
//                TrafficTagHolder.setTrafficTag(trafficTag);
//            }
//            System.out.println("请求头为:" + trafficTag);
//            chain.doFilter(request, response);
//        } finally {
//            TrafficTagHolder.clear();
//        }
//    }
//}