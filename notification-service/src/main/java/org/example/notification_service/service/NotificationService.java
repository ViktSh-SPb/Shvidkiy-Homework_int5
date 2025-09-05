package org.example.notification_service.service;

import org.example.commonevents.dto.UserEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void processEvent(UserEvent event){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject(event.getOperation().getSubject());
        message.setText(event.getOperation().getText());
        mailSender.send(message);
    }
}
