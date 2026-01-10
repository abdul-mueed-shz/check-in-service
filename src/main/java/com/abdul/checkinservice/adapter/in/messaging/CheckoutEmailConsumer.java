package com.abdul.checkinservice.adapter.in.messaging;

import com.abdul.checkinservice.domain.common.model.EmployeeTrackedHoursDto;
import com.abdul.checkinservice.domain.email.port.in.SendTrackedHoursEmailUseCase;
import com.abdul.checkinservice.domain.user.model.UserDto;
import com.abdul.checkinservice.domain.user.port.in.GetUserByEmployeeId;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutEmailConsumer {
    private final SendTrackedHoursEmailUseCase sendTrackedHoursEmailUseCase;
    private final GetUserByEmployeeId getUserByEmployeeId;

    @KafkaListener(
            topics = "#{'${spring.kafka.topics.emails-topic}'}",
            groupId = "#{'${spring.kafka.groups.email-group}'}",
            containerFactory = "emailKafkaListenerContainerFactory"
    )
    public void listen(EmployeeTrackedHoursDto employeeTrackedHoursDto) throws MessagingException {
        UserDto userDto = getUserByEmployeeId.execute(employeeTrackedHoursDto.employeeId());
        sendTrackedHoursEmailUseCase.execute(
                userDto.getEmail(),
                userDto.getDisplayName(),
                employeeTrackedHoursDto.trackedHours());
    }
}
