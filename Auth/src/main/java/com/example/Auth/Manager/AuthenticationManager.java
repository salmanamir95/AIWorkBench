package com.example.Auth.Manager;

import com.example.Auth.api.dto.AuthResponse;
import com.example.Auth.api.dto.UserCredentials;
import com.example.Auth.db.models.RefreshToken;
import com.example.Auth.db.models.UserAuth;
import com.example.Auth.db.service.RefreshTokenService;
import com.example.Auth.provider.AuthenticationProvider;
import com.example.Auth.security.JwtService;
import com.example.Auth.verifier.AuthenticationVerifier;

public class AuthenticationManager {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationVerifier authenticationVerifier;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationManager(
            final AuthenticationProvider authenticationProvider,
            final AuthenticationVerifier authenticationVerifier,
            final JwtService jwtService,
            final RefreshTokenService refreshTokenService
    ) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationVerifier = authenticationVerifier;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public UserAuth authenticate(
            final String email,
            final String password
    ) {

        // Step 1: Validate credentials
        final UserAuth user = authenticationProvider.authenticate(email, password);

        // Step 2: Verify account state
        authenticationVerifier.verify(user);

        return user;
    }

    public UserAuth authenticateJwtAccess(final String token) {
        if (!jwtService.isTokenValid(token) || !"access".equals(jwtService.extractType(token))) {
            throw new IllegalArgumentException("Invalid access token");
        }
        final String email = jwtService.extractEmail(token);
        final UserAuth user = authenticationProvider.loadByEmail(email);
        authenticationVerifier.verify(user);
        return user;
    }

    public AuthResponse authenticateAndIssueTokens(final String email, final String password) {
        final UserAuth user = authenticate(email, password);
        return issueTokens(user);
    }

    public AuthResponse refreshTokens(final String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken) || !"refresh".equals(jwtService.extractType(refreshToken))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        final RefreshToken stored = refreshTokenService.getValidToken(refreshToken);
        final UserAuth user = stored.getUserAuth();

        refreshTokenService.revoke(stored);
        return issueTokens(user);
    }

    public void logout(final String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken) || !"refresh".equals(jwtService.extractType(refreshToken))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        final RefreshToken stored = refreshTokenService.getValidToken(refreshToken);
        refreshTokenService.revoke(stored);
    }

    public void revokeAllForUser(final UserAuth user) {
        refreshTokenService.revokeAllForUser(user);
    }

    public AuthResponse issueTokens(final UserAuth user) {
        final String accessToken = jwtService.generateAccessToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.create(user, refreshToken, jwtService.extractExpiration(refreshToken));

        return new AuthResponse(
                UserCredentials.from(user),
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpirationMs(),
                jwtService.getRefreshTokenExpirationMs(),
                "Bearer"
        );
    }
}
