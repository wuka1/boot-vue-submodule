package com.example.demoservice;

import com.example.common.loadbalancer.GrayLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableFeignClients(basePackages = "com.example")
@LoadBalancerClients(defaultConfiguration = GrayLoadBalancerConfiguration.class) //默认所有
public class DemoServiceGrayApplication {

    public static void main(String[] args) {
        System.setProperty("csp.sentinel.log.dir", "./logs/sentinel");
        SpringApplication.run(DemoServiceGrayApplication.class, args);
    }

}
