package com.example.Auth.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

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
        final MimeMessage message = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            if (from != null && !from.isBlank()) {
                helper.setFrom(parseFrom(from));
            }
            helper.setSubject("Your verification code");
            helper.setText("Your OTP code is: " + code + "\n\nThis code will expire soon.", false);
            mailSender.send(message);
        } catch (final MessagingException | UnsupportedEncodingException ex) {
            final String detail = ex.getMessage() == null ? "Unknown error" : ex.getMessage();
            throw new IllegalStateException("Failed to send email: " + detail, ex);
        }
    }

    private InternetAddress parseFrom(final String raw) throws MessagingException, UnsupportedEncodingException {
        final String trimmed = raw.trim();
        if (trimmed.contains("<") && trimmed.contains(">")) {
            final int start = trimmed.indexOf('<');
            final int end = trimmed.indexOf('>');
            final String name = trimmed.substring(0, start).trim();
            final String email = trimmed.substring(start + 1, end).trim();
            return name.isEmpty() ? new InternetAddress(email) : new InternetAddress(email, name);
        }
        return new InternetAddress(trimmed);
    }
}
