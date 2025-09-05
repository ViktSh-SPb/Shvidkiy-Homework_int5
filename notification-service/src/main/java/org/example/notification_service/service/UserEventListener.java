package org.example.notification_service.service;

import org.example.commonevents.dto.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author Viktor Shvidkiy
 */
@Service
public class UserEventListener {
    private final NotificationService notificationService;

    public UserEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user-events",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(UserEvent event) {
        notificationService.processEvent(event);
    }

}
