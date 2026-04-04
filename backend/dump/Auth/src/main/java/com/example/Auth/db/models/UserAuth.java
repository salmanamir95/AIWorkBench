package com.example.Auth.db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(
        name = "user_auth",
        indexes = {
            @Index(name = "idx_user_email", columnList = "email"),
            @Index(name = "idx_user_email_verified", columnList = "emailVerified"),
            @Index(name = "idx_user_account_locked", columnList = "accountLocked")
        },
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        }
)
public class UserAuth extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------- AUTHENTICATION INFO ----------
    @Column(nullable = false, length = 150)
    @Email
    @NotBlank
    @Size(max = 150)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    @NotNull
    private boolean emailVerified = false;

    @Column(nullable = false)
    @NotNull
    private boolean accountLocked = false;

    // ---------- CONSTRUCTORS ----------
    protected UserAuth() {}

    public UserAuth(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    // ---------- GETTERS ----------
    @Override
    public Long getId() { return id; }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isEmailVerified() { return emailVerified; }
    public boolean isAccountLocked() { return accountLocked; }

    // ---------- DOMAIN METHODS ----------
    public void changePassword(final String newPassword) { this.password = newPassword; }
    public void verifyEmail() { this.emailVerified = true; }
    public void lockAccount() { this.accountLocked = true; }
    public void unlockAccount() { this.accountLocked = false; }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (this.password == null || this.password.isBlank() || !this.password.startsWith("$argon2")) {
            throw new IllegalStateException("Password must be hashed before persisting");
        }
        if (this.email == null || this.email.isBlank() || !this.email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalStateException("Email format is invalid");
        }
    }
}
