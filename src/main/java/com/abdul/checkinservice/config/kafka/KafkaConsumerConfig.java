package com.abdul.checkinservice.config.kafka;

import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, EmployeeTrackedHoursDto> emailConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(kafkaProperties.getGroups().getEmailGroup())
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmployeeTrackedHoursDto> emailKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmployeeTrackedHoursDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(emailConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, EmployeeTrackedHoursDto> recordingServiceConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerProps(kafkaProperties.getGroups().getRecordingServiceGroup())
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmployeeTrackedHoursDto> recordingServiceKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmployeeTrackedHoursDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(recordingServiceConsumerFactory());
        return factory;
    }

    private Map<String, Object> consumerProps(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.abdul.checkinservice");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, EmployeeTrackedHoursDto.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return props;
    }
}
