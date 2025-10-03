package com.example.common.loadbalancer;

import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/31
 * *@Version 1.0
 **/

//@Configuration
public class GrayLoadBalancerConfiguration {

    @Bean
    public ReactorServiceInstanceLoadBalancer grayLoadBalancer(Environment env,
                                                               LoadBalancerClientFactory factory) {
        String name = env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new GrayLoadBalancer(
                factory.getLazyProvider(name, ServiceInstanceListSupplier.class),
                name
        );
    }

}
