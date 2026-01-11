package com.abdul.checkinservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Shift shift;
    private Map<String, Service> services = new HashMap<>();

    public List<Service> getAllCircuitBreakerServices() {
        return services.values().stream().toList();
    }

    @Data
    public static class Shift {
        private Integer maxDuration;
    }

    @Data
    public static class Service {
        private String name;
        private String listenerId;
        private Boolean circuitBreakerEnabled;
        private String url;
    }
}
