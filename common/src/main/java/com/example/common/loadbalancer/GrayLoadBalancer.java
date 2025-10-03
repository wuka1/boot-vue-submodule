package com.example.common.loadbalancer;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/31
 * *@Version 1.0
 **/
@Slf4j
public class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String serviceId;
    // 定义一个轮询策略的种子
    private final AtomicInteger position;

    private final static String TRAFFIC_TAG = "X-Traffic-Tag";

    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this(serviceInstanceListSupplierProvider,serviceId,new Random().nextInt(1000));
    }

    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                            String serviceId, int seedPosition) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        this.position = new AtomicInteger(seedPosition);
    }


    // 这个服务是Loadbalancer的标准接口，也是负载均衡策略选择服务器的入口方法
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(serviceInstances, request));
    }

    /**
     * 对服务列表进行灰度逻辑处理
     */
    private Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> serviceInstances,
                                                              Request request){
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances, request);
        if (serviceInstanceResponse == null || !serviceInstanceResponse.hasServer()) {
            log.warn("No suitable instance found for service: {}", serviceId);
        } else {
            log.debug("Chosen instance: {} for service: {}", serviceInstanceResponse.getServer().getUri(), serviceId);
        }
        return serviceInstanceResponse;

    }


    // 根据金丝雀的规则返回目标节点
Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {
        // 注册中心无可用实例 返回空
        if (CollectionUtils.isEmpty(instances)) {
            log.warn("No instance available {}", serviceId);
            return new EmptyResponse();
        }
        DefaultRequestContext context = (DefaultRequestContext) request.getContext();
        RequestData requestData = (RequestData) context.getClientRequest();
        HttpHeaders headers = requestData.getHeaders();
        // 获取到header中的流量标记
        String trafficTag = headers.getFirst(TRAFFIC_TAG);

        // 如果没有找到打标标记，或者标记为空，则使用RoundRobin规则进行查找
        if (StrUtil.isBlank(trafficTag)) {
            // 过滤掉所有金丝雀测试的节点，即Nacos Metadaba中包含流量标记的节点
            // 从剩余的节点中进行RoundRobin查找
            List<ServiceInstance> noneGrayInstances = instances.stream()
                    .filter(e -> !e.getMetadata().containsKey(TRAFFIC_TAG))
                    .collect(Collectors.toList());
            return getRoundRobinInstance(noneGrayInstances);
        }

        // 如果WelClient的Header里包含流量标记
        // 循环每个Nacos服务节点，过滤出metadata值相同的instance，再使用RoundRobin查找
        List<ServiceInstance> grayInstances = instances.stream().filter(e -> {
            String trafficTagInMetadata = e.getMetadata().get(TRAFFIC_TAG);
            return StrUtil.equalsIgnoreCase(trafficTagInMetadata, trafficTag);
        }).collect(Collectors.toList());
        return getRoundRobinInstance(grayInstances);
    }

    // 使用RoundRobin机制获取节点
    private Response<ServiceInstance> getRoundRobinInstance(List<ServiceInstance> instances) {
        // 如果没有可用节点，则返回空
        if (instances.isEmpty()) {
            log.warn("No servers available for service: " + serviceId);
            return new EmptyResponse();
        }

        // 每一次计数器都自动+1，实现轮询的效果
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance instance = instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }
}
