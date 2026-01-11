package com.abdul.checkinservice.adapter.in.messaging;

import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import com.abdul.checkinservice.domain.email.port.in.SendTrackedHoursEmailUseCase;
import com.abdul.checkinservice.domain.timesheet.enums.EmailStatusEnum;
import com.abdul.checkinservice.domain.timesheet.port.in.UpdateTimeSheetUseCase;
import com.abdul.checkinservice.domain.user.model.UserDto;
import com.abdul.checkinservice.domain.user.port.in.GetUserByEmployeeId;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CheckoutEmailConsumer {
    private final SendTrackedHoursEmailUseCase sendTrackedHoursEmailUseCase;
    private final GetUserByEmployeeId getUserByEmployeeId;
    private final UpdateTimeSheetUseCase updateTimeSheetUseCase;

    @RetryableTopic(
            backoff = @Backoff(delay = 3000L, multiplier = 1.5),
            attempts = "5",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            include = {MessagingException.class}
    )
    @KafkaListener(
            topics = "#{'${spring.kafka.topics.emails-topic}'}",
            groupId = "#{'${spring.kafka.groups.email-group}'}",
            containerFactory = "emailKafkaListenerContainerFactory"
    )
    public void listen(EmployeeTrackedHoursDto employeeTrackedHoursDto,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(value = KafkaHeaders.EXCEPTION_MESSAGE,
                               required = false) String exceptionMessage) throws MessagingException {
        log.info("Processing email message for topic {}, to send a confirmation to employee {}", topic, employeeTrackedHoursDto.employeeId());

        if (exceptionMessage != null) {
            log.warn("Trying to send an email again, Previous known exception: {}", exceptionMessage);
        }

        UserDto userDto = getUserByEmployeeId.execute(employeeTrackedHoursDto.employeeId());
        sendTrackedHoursEmailUseCase.execute(
                userDto.getEmail(),
                userDto.getDisplayName(),
                employeeTrackedHoursDto.trackedHours());
        log.info("Email sent successfully to employee: {} with email: {}",
                employeeTrackedHoursDto.employeeId(),
                userDto.getEmail());

        updateTimeSheetUseCase.updateEmailDeliveryStatus(
                employeeTrackedHoursDto.recordId(),
                EmailStatusEnum.SENT
        );
        log.info("Timesheet status for email delivery set to {} for employee {}",
                EmailStatusEnum.SENT,
                employeeTrackedHoursDto.employeeId());
    }

    @DltHandler
    public void handleDlt(EmployeeTrackedHoursDto employeeTrackedHoursDto,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage) {
        log.error("Email message moved to DLT for Topic: {}, EmployeeId: {}, Error: {}",
                topic, employeeTrackedHoursDto.employeeId(), exceptionMessage);

        updateTimeSheetUseCase.updateEmailDeliveryStatus(
                employeeTrackedHoursDto.recordId(),
                EmailStatusEnum.FAILED
        );
        log.warn("Timesheet status for email delivery set to {} for employee {}",
                EmailStatusEnum.FAILED,
                employeeTrackedHoursDto.employeeId());
    }
}
