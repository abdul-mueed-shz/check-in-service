package com.abdul.checkinservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
@EnableKafkaRetryTopic
public class CheckInServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckInServiceApplication.class, args);
    }

}
