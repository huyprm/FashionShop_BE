package org.ptithcm2021.fashionshop.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.EmailRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendEmail {
    private static JavaMailSender mailSender;

    public SendEmail(JavaMailSender mailSender) {
        SendEmail.mailSender = mailSender;
    }

    public static String sendEmail(EmailRequest emailRequest) {
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
