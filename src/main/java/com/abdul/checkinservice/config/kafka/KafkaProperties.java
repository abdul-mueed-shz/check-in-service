package com.abdul.checkinservice.config.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    private boolean enabled;

    private String bootstrapServers;

    private Group groups;

    private Topics topics;

    @Data
    public static class Group {

        private String emailGroup;
        private String recordingServiceGroup;
    }

    @Data
    public static class Topics {
        private String emailsTopic;
        private String recordingServiceTopic;

        public List<String> getAllTopics() {
            return List.of(emailsTopic, recordingServiceTopic);
        }
    }
}
