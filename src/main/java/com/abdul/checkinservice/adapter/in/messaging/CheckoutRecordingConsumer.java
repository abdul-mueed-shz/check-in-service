package com.abdul.checkinservice.adapter.in.messaging;

import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckoutRecordingConsumer {
    
    @KafkaListener(
            topics = "#{'${spring.kafka.topics.recording-service-topic}'}",
            groupId = "#{'${spring.kafka.groups.recording-service-group}'}",
            containerFactory = "recordingServiceKafkaListenerContainerFactory"
    )
    @RetryableTopic(
            backoff = @Backoff(multiplier = 2.0, maxDelay = 10000L),
            attempts = "5"
    )
    public void listen(EmployeeTrackedHoursDto employeeTrackedHoursDto) {
        // Todo: Implement mock Api call
        System.out.println(employeeTrackedHoursDto);
    }
}
