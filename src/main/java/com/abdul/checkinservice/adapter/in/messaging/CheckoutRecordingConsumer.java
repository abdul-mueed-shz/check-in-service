package com.abdul.checkinservice.adapter.in.messaging;

import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutRecordingConsumer {
    @KafkaListener(
            topics = "#{'${spring.kafka.topics.recording-service-topic}'}",
            groupId = "#{'${spring.kafka.groups.recording-service-group}'}",
            containerFactory = "recordingServiceKafkaListenerContainerFactory"
    )
    public void listen(EmployeeTrackedHoursDto employeeTrackedHoursDto) throws MessagingException {
        // Todo: Implement mock Api call
        System.out.println(employeeTrackedHoursDto);
    }
}
