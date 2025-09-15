package org.example.notification_service;

import jakarta.mail.internet.MimeMessage;
import org.example.commonevents.dto.Operation;
import org.example.commonevents.dto.UserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Viktor Shvidkiy
 */
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user-events"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserEventListenerIntegrationTest {
    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    private TestMailSender mailSender;

    @BeforeEach
    void setup(){
        mailSender.clear();
    }

    @Test
    void testUserEventCreateSendsEmail(){
        UserEvent event = new UserEvent();
        event.setOperation(Operation.CREATE);
        event.setEmail("test@example.com");

        kafkaTemplate.send("user-events", event);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(()->{
            assertEquals(1, mailSender.getMessages().size());
            SimpleMailMessage message=mailSender.getMessages().get(0);
            assertEquals("test@example.com", message.getTo()[0]);
            assertEquals("Аккаунт создан", message.getSubject());
            assertEquals("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.", message.getText());
        });
    }

    @Test
    void testUserEventDeleteSendsEmail(){
        UserEvent event = new UserEvent();
        event.setOperation(Operation.DELETE);
        event.setEmail("test@example.com");

        kafkaTemplate.send("user-events", event);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(()->{
            assertEquals(1, mailSender.getMessages().size());
            SimpleMailMessage message=mailSender.getMessages().get(0);
            assertEquals("test@example.com", message.getTo()[0]);
            assertEquals("Аккаунт удален", message.getSubject());
            assertEquals("Здравствуйте! Ваш аккаунт был удалён.", message.getText());
        });
    }

    @TestConfiguration
    static class MailTestConfig{
        @Bean
        public TestMailSender mailSender(){
            return new TestMailSender();
        }
    }

    static class TestMailSender implements JavaMailSender{

        private final List<SimpleMailMessage> messages = new ArrayList<>();

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            messages.add(simpleMessage);
        }

        public List<SimpleMailMessage> getMessages(){
            return messages;
        }

        public void clear(){
            messages.clear();
        }

        @Override
        public MimeMessage createMimeMessage() {return null;}
        @Override
        public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {return null;}
        @Override
        public void send(MimeMessage... mimeMessages) throws MailException {}
        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {}
    }
}
