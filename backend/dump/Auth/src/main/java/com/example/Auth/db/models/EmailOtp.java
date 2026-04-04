package com.example.Auth.db.models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "email_otp",
        indexes = {
                @Index(name = "idx_email_otp_email", columnList = "email"),
                @Index(name = "idx_email_otp_expires", columnList = "expiresAt"),
                @Index(name = "idx_email_otp_verified", columnList = "verified")
        }
)
public class EmailOtp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String codeHash;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private int attempts = 0;

    @Column(nullable = false)
    private int maxAttempts = 5;

    @Column(nullable = false)
    private int sentCount = 0;

    @Column
    private Instant lastSentAt;

    @Column(nullable = false)
    private boolean verified = false;

    protected EmailOtp() {}

    public EmailOtp(final String email, final String codeHash, final Instant expiresAt, final int maxAttempts) {
        this.email = email;
        this.codeHash = codeHash;
        this.expiresAt = expiresAt;
        this.maxAttempts = maxAttempts;
        this.sentCount = 1;
        this.lastSentAt = Instant.now();
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getSentCount() {
        return sentCount;
    }

    public Instant getLastSentAt() {
        return lastSentAt;
    }

    public boolean isVerified() {
        return verified;
    }

    public void incrementAttempts() {
        this.attempts += 1;
    }

    public void markVerified() {
        this.verified = true;
    }

    public void resend(final String codeHash, final Instant expiresAt) {
        this.codeHash = codeHash;
        this.expiresAt = expiresAt;
        this.sentCount += 1;
        this.lastSentAt = Instant.now();
        this.attempts = 0;
        this.verified = false;
    }

    public void resetSendCount() {
        this.sentCount = 0;
    }
}
