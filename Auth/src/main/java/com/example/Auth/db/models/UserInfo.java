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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(
        name = "users",
        indexes = {
            @Index(name = "idx_user_email", columnList = "email"),
            @Index(name = "idx_user_created_at", columnList = "createdAt"),
            @Index(name = "idx_user_email_verified", columnList = "emailVerified"),
            @Index(name = "idx_user_account_locked", columnList = "accountLocked"),
            @Index(name = "idx_user_verified_created", columnList = "emailVerified,createdAt")
        },
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        }
)
public class UserInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------- BASIC INFO ----------
    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String name;

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
    @Min(0)
    @Max(150)
    private Integer age;

    // ---------- ACCOUNT STATE ----------
    @Column(nullable = false)
    @NotNull
    private boolean emailVerified = false;

    @Column(nullable = false)
    @NotNull
    private boolean accountLocked = false;

    // ---------- CONSTRUCTORS ----------
    protected UserInfo() {
    }

    public UserInfo(
            final String name,
            final String email,
            final String password,
            final Integer age
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    // ---------- GETTERS ----------
    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    // ---------- DOMAIN METHODS ----------
    public void changeName(final String newName) {
        this.name = newName;
    }

    public void changeAge(final Integer newAge) {
        this.age = newAge;
    }

    public void changePassword(final String newPassword) {
        this.password = newPassword;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void lockAccount() {
        this.accountLocked = true;
    }

    public void unlockAccount() {
        this.accountLocked = false;
    }

    public void changeEmail(final String newEmail) {
        this.email = newEmail;
        this.emailVerified = false;
    }

    // Add inside UserInfo.java
    @PrePersist
    protected void onPrePersist() {
        validatePassword();
        validateEmail();
        validateName();
    }

    @PreUpdate
    protected void onPreUpdate() {
        validatePassword();
        validateEmail();
        validateName();
    }

// ---------- PRIVATE VALIDATORS ----------
    private void validatePassword() {
        if (this.password == null || this.password.isBlank()) {
            throw new IllegalStateException("Password cannot be null or empty");
        }
        // Argon2 encoded passwords always start with $argon2
        if (!this.password.startsWith("$argon2")) {
            throw new IllegalStateException(
                    "Password must be hashed before persisting — raw passwords are not allowed"
            );
        }
    }

    private void validateEmail() {
        if (this.email == null || this.email.isBlank()) {
            throw new IllegalStateException("Email cannot be null or empty");
        }
        if (!this.email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalStateException("Email format is invalid: " + this.email);
        }
    }

    private void validateName() {
        if (this.name == null || this.name.isBlank()) {
            throw new IllegalStateException("Name cannot be null or empty");
        }
        if (this.name.length() > 100) {
            throw new IllegalStateException("Name exceeds maximum length of 100");
        }
    }
}
