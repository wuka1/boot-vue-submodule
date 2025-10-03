package com.example.graycommon.filter;

import com.example.graycommon.utils.TrafficTagHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/8/1
 * *@Version 1.0
 **/
@Component
public class TrafficTagFeignInterceptor implements RequestInterceptor {

    private static final String TRAFFIC_TAG = "X-Traffic-Tag";

    @Override
    public void apply(RequestTemplate template) {
        String trafficTag = TrafficTagHolder.getTrafficTag();
        if (trafficTag != null && !trafficTag.isEmpty()) {
            template.header(TRAFFIC_TAG, trafficTag);
        }
    }

}