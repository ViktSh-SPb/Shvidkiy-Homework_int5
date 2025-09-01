package org.example.notification_service.config;

import org.example.notification_service.service.ConsoleMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author Viktor Shvidkiy
 */
@Configuration
public class MailConfig {
//    @Bean
//    public JavaMailSender javaMailSender(){
//        return new ConsoleMailSender();
//    }

//    @Bean
//    public JavaMailSender javaMailSender(JavaMailSenderImpl impl) {
//        return impl;
//    }
}
