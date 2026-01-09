package com.abdul.checkinservice.domain.email.usecase;

import com.abdul.checkinservice.domain.email.model.TemplateEmailRequest;
import com.abdul.checkinservice.domain.email.port.in.SendEmailUseCase;
import com.abdul.checkinservice.domain.email.port.in.SendTrackedHoursEmailUseCase;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendTrackedHoursEmailUseCaseImpl implements SendTrackedHoursEmailUseCase {
    private final SendEmailUseCase sendEmailUseCase;

    public void execute(String toEmail, String displayName, Integer trackedHours) throws MessagingException {
        Map<String, String> context = new HashMap<>();
        context.put("displayName", displayName);
        context.put("trackedHours", trackedHours.toString());
        log.info("Sending tracked hours email to user {} with tracked hours {}", displayName, trackedHours);
        sendEmailUseCase.sendTemplateEmail(
                TemplateEmailRequest.builder()
                        .recipients(Collections.singletonList(toEmail))
                        .subject("Checkout Success")
                        .templateName("timesheet-mail")
                        .templateVariables(context)
                        .build()
        );
    }
}
