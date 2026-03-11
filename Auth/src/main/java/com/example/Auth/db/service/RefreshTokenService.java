package com.example.Auth.db.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Auth.db.models.RefreshToken;
import com.example.Auth.db.models.UserAuth;
import com.example.Auth.db.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(final RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken create(final UserAuth user, final String token, final Instant expiresAt) {
        return refreshTokenRepository.save(new RefreshToken(user, token, expiresAt));
    }

    @Transactional(readOnly = true)
    public RefreshToken getValidToken(final String token) {
        final RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expired");
        }

        return refreshToken;
    }

    @Transactional
    public void revoke(final RefreshToken token) {
        token.revoke();
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void revokeAllForUser(final UserAuth user) {
        refreshTokenRepository.deleteByUserAuth(user);
    }
}
