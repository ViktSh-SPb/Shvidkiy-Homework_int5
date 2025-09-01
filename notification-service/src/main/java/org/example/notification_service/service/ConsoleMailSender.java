package org.example.notification_service.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.InputStream;

/**
 * @author Viktor Shvidkiy
 */
public class ConsoleMailSender implements JavaMailSender {
    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        System.out.println("-----Имитирую отправку письма-----");
        System.out.println("Кому: " + String.join(", ", simpleMessage.getTo()));
        System.out.println("Тема: " + simpleMessage.getSubject());
        System.out.println("Текст: " + simpleMessage.getText());
        System.out.println("----------------------------------");
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        for (SimpleMailMessage msg : simpleMessages) {
            send(msg);
        }
    }
}
