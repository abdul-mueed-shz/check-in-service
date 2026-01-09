package com.abdul.checkinservice.domain.email.port.in;

import jakarta.mail.MessagingException;

public interface SendTrackedHoursEmailUseCase {
    void execute(String toEmail, String displayName, Integer trackedHours) throws MessagingException;
}
