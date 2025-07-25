package com.example.auth.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/23
 * *@Version 1.0
 **/
@Getter
@Component
@ConfigurationProperties(prefix = "whitelist")
public class WhitelistProperties {

    /**
     * 白名单路径列表
     */
    private List<String> uri;

    public void setUri(List<String> uri) {
        this.uri = uri;
    }
}
