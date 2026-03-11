package com.example.Auth.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Auth.Manager.AuthenticationManager;
import com.example.Auth.api.dto.AuthResponse;
import com.example.Auth.api.dto.ChangePasswordRequest;
import com.example.Auth.api.dto.LoginRequest;
import com.example.Auth.api.dto.LogoutRequest;
import com.example.Auth.api.dto.RefreshTokenRequest;
import com.example.Auth.api.dto.SignUpRequest;
import com.example.Auth.api.dto.UserCredentials;
import com.example.Auth.api.response.GenericResponse;
import com.example.Auth.db.audit.AuditLogService;
import com.example.Auth.db.models.AuditLog;
import com.example.Auth.db.models.RefreshToken;
import com.example.Auth.db.models.UserAuth;
import com.example.Auth.db.service.RefreshTokenService;
import com.example.Auth.db.service.UserAuthService;
import com.example.Auth.security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;
    private final AuditLogService auditLogService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            final UserAuthService userAuthService,
            final AuthenticationManager authenticationManager,
            final AuditLogService auditLogService,
            final JwtService jwtService,
            final RefreshTokenService refreshTokenService
    ) {
        this.userAuthService = userAuthService;
        this.authenticationManager = authenticationManager;
        this.auditLogService = auditLogService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register user")
    public ResponseEntity<GenericResponse<AuthResponse>> signUp(@Valid @RequestBody final SignUpRequest request) {
        try {
            final UserAuth user = userAuthService.registerUser(request.email(), request.password());
            auditLogService.log(
                    "UserAuth",
                    user.getId(),
                    AuditLog.AuditAction.SIGNUP_SUCCESS,
                    user.getEmail(),
                    null
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(GenericResponse.success(buildAuthResponse(user), "User registered successfully"));
        } catch (final RuntimeException ex) {
            auditLogService.log(
                    "UserAuth",
                    null,
                    AuditLog.AuditAction.SIGNUP_FAILED,
                    request.email(),
                    ex.getMessage()
            );
            throw ex;
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<GenericResponse<AuthResponse>> login(@Valid @RequestBody final LoginRequest request) {
        try {
            final UserAuth user = authenticationManager.authenticate(request.email(), request.password());
            auditLogService.log(
                    "UserAuth",
                    user.getId(),
                    AuditLog.AuditAction.LOGIN_SUCCESS,
                    user.getEmail(),
                    null
            );
            return ResponseEntity.ok(GenericResponse.success(buildAuthResponse(user), "Login successful"));
        } catch (final RuntimeException ex) {
            auditLogService.log(
                    "UserAuth",
                    null,
                    AuditLog.AuditAction.LOGIN_FAILED,
                    request.email(),
                    ex.getMessage()
            );
            throw ex;
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<GenericResponse<AuthResponse>> refresh(@Valid @RequestBody final RefreshTokenRequest request) {
        final String refreshToken = request.refreshToken();
        if (!jwtService.isTokenValid(refreshToken) || !"refresh".equals(jwtService.extractType(refreshToken))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        final RefreshToken stored = refreshTokenService.getValidToken(refreshToken);
        final UserAuth user = stored.getUserAuth();

        // Rotate refresh token
        refreshTokenService.revoke(stored);
        final AuthResponse response = buildAuthResponse(user);
        return ResponseEntity.ok(GenericResponse.success(response, "Token refreshed"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<GenericResponse<Void>> logout(@Valid @RequestBody final LogoutRequest request) {
        final String refreshToken = request.refreshToken();
        if (!jwtService.isTokenValid(refreshToken) || !"refresh".equals(jwtService.extractType(refreshToken))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        final RefreshToken stored = refreshTokenService.getValidToken(refreshToken);
        refreshTokenService.revoke(stored);
        return ResponseEntity.ok(GenericResponse.success("Logged out successfully"));
    }

    @PatchMapping("/users/{id}/password")
    @Operation(summary = "Change user password")
    public ResponseEntity<GenericResponse<Void>> changePassword(
            @PathVariable final Long id,
            @Valid @RequestBody final ChangePasswordRequest request
    ) {
        try {
            final UserAuth user = userAuthService.changePassword(id, request.newPassword());
            auditLogService.log(
                    "UserAuth",
                    user.getId(),
                    AuditLog.AuditAction.PASSWORD_CHANGE_SUCCESS,
                    user.getEmail(),
                    null
            );
            refreshTokenService.revokeAllForUser(user);
            return ResponseEntity.ok(GenericResponse.success("Password changed successfully"));
        } catch (final RuntimeException ex) {
            auditLogService.log(
                    "UserAuth",
                    id,
                    AuditLog.AuditAction.PASSWORD_CHANGE_FAILED,
                    "userId:" + id,
                    ex.getMessage()
            );
            throw ex;
        }
    }

    private AuthResponse buildAuthResponse(final UserAuth user) {
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
