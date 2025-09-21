package org.example.user_service.service;

import org.example.commonevents.dto.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Viktor Shvidkiy
 */
@Service
public class UserKafkaProducer {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserKafkaProducer(KafkaTemplate<String, UserEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(UserEvent event){
        kafkaTemplate.send("user-events", event.getEmail(), event);
    }
}
