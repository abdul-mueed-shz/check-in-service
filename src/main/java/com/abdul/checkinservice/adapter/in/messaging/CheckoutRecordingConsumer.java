package com.abdul.checkinservice.adapter.in.messaging;

import com.abdul.checkinservice.adapter.out.api.RecordingServiceApiClient;
import com.abdul.checkinservice.adapter.out.exception.RecordingServiceException;
import com.abdul.checkinservice.config.kafka.KafkaListenerContainerManager;
import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import com.abdul.checkinservice.domain.timesheet.enums.RecordingServiceAckEnum;
import com.abdul.checkinservice.domain.timesheet.port.in.UpdateTimeSheetUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for recording service notifications.
 * Uses circuit breaker to handle external service outages gracefully.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CheckoutRecordingConsumer {

    private final UpdateTimeSheetUseCase updateTimeSheetUseCase;
    private final RecordingServiceApiClient recordingServiceApiClient;

    @RetryableTopic(
            backoff = @Backoff(delay = 2000L, multiplier = 2.0, maxDelay = 30000L),
            attempts = "10",
            include = {RecordingServiceException.class}
    )
    @KafkaListener(
            id = KafkaListenerContainerManager.RECORDING_SERVICE_LISTENER_ID,
            topics = "${spring.kafka.topics.recording-service-topic}",
            groupId = "${spring.kafka.groups.recording-service-group}",
            containerFactory = "recordingServiceKafkaListenerContainerFactory"
    )
    public void listen(EmployeeTrackedHoursDto message,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        log.info("Processing recording service notification - topic: {}, recordId: {}, employeeId: {}",
                topic, message.recordId(), message.employeeId());
        recordingServiceApiClient.notifyRecordingService(message);
        updateTimeSheetUseCase.updateRecordServiceAcknowledgementStatus(
                message.recordId(),
                RecordingServiceAckEnum.NOTIFIED
        );
        log.info("Recording service notified successfully for recordId: {}", message.recordId());
    }

    @DltHandler
    public void handleDeadLetterMessage(EmployeeTrackedHoursDto message,
                                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                        @Header(value = KafkaHeaders.EXCEPTION_MESSAGE, required = false) String errorMessage) {

        log.error("Recording service acknowledgement failed after all retries - topic: {}, recordId: {}, error: {}",
                topic, message.recordId(), errorMessage);
        updateTimeSheetUseCase.updateRecordServiceAcknowledgementStatus(
                message.recordId(),
                RecordingServiceAckEnum.FAILED
        );
    }
}
