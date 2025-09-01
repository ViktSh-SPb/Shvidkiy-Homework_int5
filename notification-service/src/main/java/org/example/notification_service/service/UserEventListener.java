package org.example.notification_service.service;

import org.example.commonevents.dto.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Viktor Shvidkiy
 */
@Service
public class UserEventListener {
    private final JavaMailSender mailSender;

    public UserEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "user-events",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(UserEvent event) {
        String subject;
        String text;

        if ("CREATE".equals(event.getOperation())) {
            subject = "Аккаунт создан";
            text = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
        } else if ("DELETE".equals(event.getOperation())) {
            subject = "Аккаунт удален";
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
