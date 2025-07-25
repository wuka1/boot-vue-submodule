package com.example.configure;

import com.example.service.IpCountService;
import com.example.service.impl.IpCountServiceImpl;
import org.springframework.context.annotation.Bean;

public class IPAutoConfiguration {

    @Bean
    protected IpCountService ipCountService(){
        return  new IpCountServiceImpl();
    }
}
