package com.example.Auth.api.dto;

import java.time.Instant;

import com.example.Auth.db.models.UserInfo;

public record UserResponse(
        Long id,
        String name,
        String email,
        Integer age,
        boolean emailVerified,
        boolean accountLocked,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse from(final UserInfo user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.isEmailVerified(),
                user.isAccountLocked(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
