package com.abdul.checkinservice.domain.email.usecase;

import com.abdul.checkinservice.domain.email.model.TemplateEmailRequest;
import com.abdul.checkinservice.domain.email.port.in.SendEmailUseCase;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SendEmailUseCaseImpl implements SendEmailUseCase {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async("threadPoolTaskExecutor")
    @Override
    public void sendTextEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Async("threadPoolTaskExecutor")
    public void sendTemplateEmail(TemplateEmailRequest request) throws MessagingException {

        Context context = new Context();
        if (request.getTemplateVariables() != null) {
            request.getTemplateVariables().forEach(context::setVariable);
        }

        String htmlContent = templateEngine.process(request.getTemplateName(), context);

        for (String recipient : request.getRecipients()) {
            sendHtmlEmail(recipient, request.getSubject(), htmlContent);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
