package com.example.Auth.api.dto;

public record AuthResponse(
        UserCredentials user,
        String accessToken,
        String refreshToken,
        long accessTokenExpiresIn,
        long refreshTokenExpiresIn,
        String tokenType
) {
}
