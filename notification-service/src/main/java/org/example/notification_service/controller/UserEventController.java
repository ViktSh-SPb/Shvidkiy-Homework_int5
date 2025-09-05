package org.example.notification_service.controller;

import org.example.commonevents.dto.UserEvent;
import org.example.notification_service.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class UserEventController {
    private final NotificationService notificationService;

    public UserEventController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> receiveEvent(@RequestBody UserEvent event){
        notificationService.processEvent(event);
        return ResponseEntity.ok("Сообщение отправлено.");
    }
}
