package com.example.Auth.api.dto;

import java.time.Instant;

import com.example.Auth.db.models.UserAuth;

public record UserCredentials
(
        Long id,
        String email,
        boolean emailVerified,
        boolean accountLocked,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserCredentials from(final UserAuth user) {
        return new UserCredentials(
                user.getId(),
                user.getEmail(),
                user.isEmailVerified(),
                user.isAccountLocked(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
