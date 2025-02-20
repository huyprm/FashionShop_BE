package org.ptithcm2021.fashionshop.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.EmailRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Setter
@Getter
public class MailUtils {
    private JavaMailSender mailSender;

    public MailUtils(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendEmail(EmailRequest emailRequest) {
        if (mailSender == null) {
            throw new IllegalStateException("JavaMailSender has not been initialized.");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getTo());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());


        mailSender.send(message);
        return "Email sent";
    }
}
