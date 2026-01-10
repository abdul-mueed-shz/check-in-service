package com.abdul.checkinservice.adapter.in.messaging;

import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import com.abdul.checkinservice.domain.timesheet.enums.RecordingServiceAckEnum;
import com.abdul.checkinservice.domain.timesheet.port.in.UpdateTimeSheetUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckoutRecordingConsumer {
    private final UpdateTimeSheetUseCase updateTimeSheetUseCase;

    @RetryableTopic(
            backoff = @Backoff(delay = 1000L, multiplier = 2.0, maxDelay = 10000L),
            attempts = "3",
            include = {Exception.class}
    )
    @KafkaListener(
            topics = "#{'${spring.kafka.topics.recording-service-topic}'}",
            groupId = "#{'${spring.kafka.groups.recording-service-group}'}",
            containerFactory = "recordingServiceKafkaListenerContainerFactory"
    )
    public void listen(EmployeeTrackedHoursDto employeeTrackedHoursDto,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Received message on topic: {}, employeeId: {}", topic, employeeTrackedHoursDto.employeeId());
        // Todo: Implement mock Api call
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Service unavailable");
    }

    @DltHandler
    public void handleDlt(EmployeeTrackedHoursDto employeeTrackedHoursDto,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage) {
        log.error("Message moved to DLT. Topic: {}, EmployeeId: {}, Error: {}",
                topic, employeeTrackedHoursDto.employeeId(), exceptionMessage);
        updateTimeSheetUseCase.updateRecordServiceAcknowledgementStatus(
                employeeTrackedHoursDto.recordId(),
                RecordingServiceAckEnum.FAILED
        );
    }
}
