package com.abdul.checkinservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Shift shift;

    @Data
    public static class Shift {
        private Float maxDuration;
    }
}
