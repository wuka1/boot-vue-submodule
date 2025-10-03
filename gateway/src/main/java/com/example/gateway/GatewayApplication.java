package com.example.gateway;

import com.example.common.loadbalancer.GrayLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@SpringBootApplication(scanBasePackages = "com.example")
//@LoadBalancerClient(name = "demo-service", configuration = GrayLoadBalancerConfiguration.class) // 指定负载的服务
@LoadBalancerClients(defaultConfiguration = GrayLoadBalancerConfiguration.class) //默认所有
public class GatewayApplication {

    public static void main(String[] args) {
        // 设置sentinel日志，相对或绝对路径，推荐在工程jar -jar xx.jar -Dcsp.sentinel.log.dir=./logs/sentinel
        System.setProperty("csp.sentinel.log.dir", "./logs/sentinel");
        SpringApplication.run(GatewayApplication.class, args);
    }

}
