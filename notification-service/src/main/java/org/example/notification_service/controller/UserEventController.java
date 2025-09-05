package org.example.notification_service.controller;

import org.example.commonevents.dto.UserEvent;
import org.example.notification_service.service.UserEventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class UserEventController {
    private final UserEventListener userEventListener;

    public UserEventController(UserEventController userEventController, UserEventListener userEventListener) {
        this.userEventListener = userEventListener;
    }

    public ResponseEntity<String> receiveEvent(@RequestBody UserEvent event){
        userEventListener.processEvent(event);
        return ResponseEntity.ok("Сообщение отправлено.");
    }
}
