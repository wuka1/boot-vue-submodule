package com.example.common.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/8/1
 * *@Version 1.0
 **/
public class GrayVersionLoadBalancer extends RoundRobinLoadBalancer {

    public GrayVersionLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        super(serviceInstanceListSupplierProvider, serviceId);
    }

//    @Override
//    public Mono<Response<ServiceInstance>> choose(Request request) {
//        // 获取请求中的灰度标记
//        String grayVersion = ((RequestDataContext) request.getContext())
//                .getClientRequest().getHeaders().getFirst("X-Traffic-Tag");
//
//        return super.choose(request).map(response -> {
//            if (!"gray".equals(grayVersion)) return response;
//
//            // 筛选灰度实例
//            List<ServiceInstance> instances = response.getServerInstances()
//                    .stream()
//                    .filter(inst -> "gray".equals(inst.getMetadata().get("version")))
//                    .collect(Collectors.toList());
//
//            return new DefaultResponse(instances.get(new Random().nextInt(instances.size()));
//        });
//    }
}
