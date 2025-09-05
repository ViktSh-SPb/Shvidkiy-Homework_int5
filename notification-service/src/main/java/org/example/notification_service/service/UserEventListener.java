package org.example.notification_service.service;

import org.example.commonevents.dto.Operation;
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject(event.getOperation().getSubject());
        message.setText(event.getOperation().getText());
        mailSender.send(message);
    }
}
