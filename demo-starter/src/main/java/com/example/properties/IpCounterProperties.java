package com.example.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("count.ip")
@Component
public class IpCounterProperties {

    private String display = DisplayMode.SIMPLE.value;

    @Getter
    public enum DisplayMode{
        SIMPLE("simple"),
        DETAIL("detail");

        private String value;

        DisplayMode(String value) {
            this.value = value;
        }

    }
}
