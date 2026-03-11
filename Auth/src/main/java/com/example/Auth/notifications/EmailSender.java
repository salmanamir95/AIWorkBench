package com.example.Auth.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private final JavaMailSender mailSender;
    private final String from;

    public EmailSender(
            final JavaMailSender mailSender,
            @Value("${mail.from}") final String from
    ) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendOtp(final String to, final String code) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject("Your verification code");
        message.setText("Your OTP code is: " + code + "\n\nThis code will expire soon.");
        mailSender.send(message);
    }
}
