package org.ptithcm2021.fashionshop;

import org.ptithcm2021.fashionshop.dto.request.EmailRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class FashionShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(FashionShopApplication.class, args);
    }


}
