package com.example.Auth.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.Auth.db.models.UserAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final Key signingKey;
    private final String issuer;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") final String secret,
            @Value("${jwt.issuer:AuthService}") final String issuer,
            @Value("${jwt.access-token-expiration-ms:900000}") final long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms:604800000}") final long refreshTokenExpirationMs
    ) {
        if (secret == null || secret.trim().length() < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 characters");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.trim().getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(final UserAuth user) {
        return buildToken(user, "access", accessTokenExpirationMs);
    }

    public String generateRefreshToken(final UserAuth user) {
        return buildToken(user, "refresh", refreshTokenExpirationMs);
    }

    public String extractEmail(final String token) {
        return parse(token).getBody().getSubject();
    }

    public Instant extractExpiration(final String token) {
        return parse(token).getBody().getExpiration().toInstant();
    }

    public String extractType(final String token) {
        final Object typ = parse(token).getBody().get("typ");
        return typ == null ? null : typ.toString();
    }

    public boolean isTokenValid(final String token) {
        final Claims claims = parse(token).getBody();
        return claims.getExpiration() != null && claims.getExpiration().after(new Date());
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }

    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }

    private String buildToken(final UserAuth user, final String type, final long expirationMs) {
        final Date now = new Date();
        final Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer(issuer)
                .claim("uid", user.getId())
                .claim("typ", type)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Jws<Claims> parse(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }
}
