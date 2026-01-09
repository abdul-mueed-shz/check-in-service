package com.abdul.checkinservice.domain.email.port.in;

import com.abdul.checkinservice.domain.email.model.TemplateEmailRequest;
import jakarta.mail.MessagingException;

public interface SendEmailUseCase {
    void sendTextEmail(String toEmail, String subject, String body);

    void sendTemplateEmail(TemplateEmailRequest request) throws MessagingException;
}
