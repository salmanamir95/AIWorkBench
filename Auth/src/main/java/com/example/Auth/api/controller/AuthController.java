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
import com.example.Auth.api.response.GenericResponse;
import com.example.Auth.db.audit.AuditLogService;
import com.example.Auth.db.models.AuditLog;
import com.example.Auth.db.models.UserAuth;
import com.example.Auth.db.service.UserAuthService;

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

    public AuthController(
            final UserAuthService userAuthService,
            final AuthenticationManager authenticationManager,
            final AuditLogService auditLogService
    ) {
        this.userAuthService = userAuthService;
        this.authenticationManager = authenticationManager;
        this.auditLogService = auditLogService;
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
                    .body(GenericResponse.success(authenticationManager.issueTokens(user), "User registered successfully"));
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
            final AuthResponse response = authenticationManager.authenticateAndIssueTokens(request.email(), request.password());
            auditLogService.log(
                    "UserAuth",
                    response.user().id(),
                    AuditLog.AuditAction.LOGIN_SUCCESS,
                    response.user().email(),
                    null
            );
            return ResponseEntity.ok(GenericResponse.success(response, "Login successful"));
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
        try {
            final AuthResponse response = authenticationManager.refreshTokens(request.refreshToken());
            auditLogService.log(
                    "RefreshToken",
                    null,
                    AuditLog.AuditAction.TOKEN_REFRESH_SUCCESS,
                    response.user().email(),
                    null
            );
            return ResponseEntity.ok(GenericResponse.success(response, "Token refreshed"));
        } catch (final RuntimeException ex) {
            auditLogService.log(
                    "RefreshToken",
                    null,
                    AuditLog.AuditAction.TOKEN_REFRESH_FAILED,
                    "refresh-token",
                    ex.getMessage()
            );
            throw ex;
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<GenericResponse<Void>> logout(@Valid @RequestBody final LogoutRequest request) {
        try {
            authenticationManager.logout(request.refreshToken());
            auditLogService.log(
                    "RefreshToken",
                    null,
                    AuditLog.AuditAction.LOGOUT_SUCCESS,
                    "refresh-token",
                    null
            );
            return ResponseEntity.ok(GenericResponse.success("Logged out successfully"));
        } catch (final RuntimeException ex) {
            auditLogService.log(
                    "RefreshToken",
                    null,
                    AuditLog.AuditAction.LOGOUT_FAILED,
                    "refresh-token",
                    ex.getMessage()
            );
            throw ex;
        }
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
            authenticationManager.revokeAllForUser(user);
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
}
