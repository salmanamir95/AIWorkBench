package com.example.Auth.db.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Auth.db.models.EmailOtp;
import com.example.Auth.db.repository.EmailOtpRepository;
import com.example.Auth.notifications.EmailSender;

@Service
public class EmailOtpService {

    private final EmailOtpRepository emailOtpRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final int otpLength;
    private final int expiryMinutes;
    private final int maxSendPerHour;
    private final int maxVerifyAttempts;
    private final int resendCooldownSeconds;
    private final SecureRandom secureRandom = new SecureRandom();

    public EmailOtpService(
            final EmailOtpRepository emailOtpRepository,
            final EmailSender emailSender,
            final PasswordEncoder passwordEncoder,
            @Value("${otp.length:6}") final int otpLength,
            @Value("${otp.expiry-minutes:10}") final int expiryMinutes,
            @Value("${otp.max-send-per-hour:3}") final int maxSendPerHour,
            @Value("${otp.max-verify-attempts:5}") final int maxVerifyAttempts,
            @Value("${otp.resend-cooldown-seconds:60}") final int resendCooldownSeconds
    ) {
        this.emailOtpRepository = emailOtpRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.otpLength = otpLength;
        this.expiryMinutes = expiryMinutes;
        this.maxSendPerHour = maxSendPerHour;
        this.maxVerifyAttempts = maxVerifyAttempts;
        this.resendCooldownSeconds = resendCooldownSeconds;
    }

    @Transactional
    public void sendOtp(final String email) {
        final EmailOtp latest = emailOtpRepository.findTopByEmailOrderByCreatedAtDesc(email).orElse(null);
        if (latest != null) {
            enforceRateLimit(latest);
        }

        final String code = generateOtp();
        final String hash = passwordEncoder.encode(code);
        final Instant expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES);

        if (latest == null) {
            emailOtpRepository.save(new EmailOtp(email, hash, expiresAt, maxVerifyAttempts));
        } else {
            latest.resend(hash, expiresAt);
            emailOtpRepository.save(latest);
        }

        emailSender.sendOtp(email, code);
    }

    @Transactional
    public void verifyOtp(final String email, final String code) {
        final EmailOtp latest = emailOtpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("OTP not requested"));

        if (latest.isVerified()) {
            throw new IllegalArgumentException("Email already verified");
        }

        if (latest.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("OTP expired");
        }

        if (latest.getAttempts() >= latest.getMaxAttempts()) {
            throw new IllegalArgumentException("Too many attempts");
        }

        if (!passwordEncoder.matches(code, latest.getCodeHash())) {
            latest.incrementAttempts();
            emailOtpRepository.save(latest);
            throw new IllegalArgumentException("Invalid OTP");
        }

        latest.markVerified();
        emailOtpRepository.save(latest);
    }

    private void enforceRateLimit(final EmailOtp latest) {
        if (latest.getLastSentAt() != null) {
            final Instant hourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
            if (latest.getLastSentAt().isBefore(hourAgo)) {
                latest.resetSendCount();
                emailOtpRepository.save(latest);
            }
            final Instant cooldownUntil = latest.getLastSentAt().plusSeconds(resendCooldownSeconds);
            if (Instant.now().isBefore(cooldownUntil)) {
                throw new IllegalArgumentException("Please wait before requesting another OTP");
            }
        }

        if (latest.getSentCount() >= maxSendPerHour) {
            throw new IllegalArgumentException("OTP send limit reached. Try later");
        }
    }

    private String generateOtp() {
        final int max = (int) Math.pow(10, otpLength);
        final int code = secureRandom.nextInt(max);
        return String.format("%0" + otpLength + "d", code);
    }
}
