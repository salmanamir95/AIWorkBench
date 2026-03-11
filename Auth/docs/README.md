# Auth Service Documentation

This document describes the **Auth** microservice: architecture, responsibilities, API surface, and why each part exists.

## Architecture And Microservice View

**Deployment context**
- This service is designed to sit behind an API Gateway and/or Nginx.
- TLS terminates at the gateway or load balancer.
- The service itself runs HTTP internally.

**High-level flow**
```
Client → API Gateway / Nginx → Auth Service → MySQL
                           ↘→ SMTP Provider
```

**What this service owns**
- User authentication (email + password).
- JWT token issuance and validation.
- Refresh token lifecycle (issue, rotate, revoke).
- Email OTP verification for email ownership.
- Audit logging for security-critical actions.

**What this service does not own**
- Frontend UI.
- API Gateway routing and TLS.
- Authorization across other services.
- OAuth or social login.

## Why This Exists

- Centralizes authentication logic and secrets in one place.
- Provides a clean contract for other services via JWT.
- Adds auditability for security and compliance.
- Encapsulates email verification flow and rate limits.

## Core Components And Responsibilities

**Controller → Service → Repository**
- `AuthController` handles HTTP and returns `GenericResponse`.
- `UserAuthService` manages user lifecycle and password change.
- `AuthenticationManager` orchestrates authentication and token flows.
- `AuthenticationProvider` verifies credentials and loads users.
- `AuthenticationVerifier` enforces account state rules.
- `JwtService` issues and validates JWTs.
- `RefreshTokenService` persists refresh tokens and handles rotation.
- `EmailOtpService` manages OTP generation, verification, and rate limits.
- `AuditLogService` records audit events.

## How It Works (Key Flows)

**Signup**
1. User registers with email + password.
2. Password is hashed with Argon2.
3. User is stored in `user_auth`.
4. JWT tokens are returned.
5. Audit log is recorded.

**Login**
1. Credentials validated by `AuthenticationProvider`.
2. Account state validated by `AuthenticationVerifier`.
3. JWT tokens returned.
4. Audit log recorded.

**JWT Access**
1. `JwtAuthenticationFilter` extracts Bearer token.
2. `AuthenticationManager.authenticateJwtAccess(...)` validates token.
3. User is loaded and account verified.

**Refresh Tokens**
1. Refresh token validated and looked up in DB.
2. Old refresh token revoked.
3. New access + refresh tokens issued.
4. Audit log recorded.

**Email Verification (OTP)**
1. OTP is generated and emailed.
2. OTP is stored hashed with expiry, attempt limits, cooldown.
3. Verification marks email as verified.
4. Audit log recorded.

## API Endpoints

Base path: `/auth`  
Swagger UI: `/swagger-ui.html`

### `POST /auth/signup`
Registers a new user.

Request:
```json
{
  "email": "user@example.com",
  "password": "strong-password"
}
```

Response:
```json
{
  "success": true,
  "data": {
    "user": {
      "id": 1,
      "email": "user@example.com",
      "emailVerified": false,
      "accountLocked": false,
      "createdAt": "2026-03-11T00:00:00Z",
      "updatedAt": "2026-03-11T00:00:00Z"
    },
    "accessToken": "jwt...",
    "refreshToken": "jwt...",
    "accessTokenExpiresIn": 900000,
    "refreshTokenExpiresIn": 604800000,
    "tokenType": "Bearer"
  },
  "msg": "User registered successfully"
}
```

### `POST /auth/login`
Verifies credentials and account status.

Request:
```json
{
  "email": "user@example.com",
  "password": "strong-password"
}
```

Response:
```json
{
  "success": true,
  "data": {
    "user": {
      "id": 1,
      "email": "user@example.com",
      "emailVerified": false,
      "accountLocked": false,
      "createdAt": "2026-03-11T00:00:00Z",
      "updatedAt": "2026-03-11T00:00:00Z"
    },
    "accessToken": "jwt...",
    "refreshToken": "jwt...",
    "accessTokenExpiresIn": 900000,
    "refreshTokenExpiresIn": 604800000,
    "tokenType": "Bearer"
  },
  "msg": "Login successful"
}
```

### `POST /auth/refresh`
Refreshes access and refresh tokens (rotation).

Request:
```json
{
  "refreshToken": "..."
}
```

### `POST /auth/logout`
Revokes a refresh token.

Request:
```json
{
  "refreshToken": "..."
}
```

### `POST /auth/verify-email/request`
Sends OTP to the email address.

Request:
```json
{
  "email": "user@example.com"
}
```

### `POST /auth/verify-email/resend`
Resends OTP, honoring cooldown and rate limits.

Request:
```json
{
  "email": "user@example.com"
}
```

### `POST /auth/verify-email/confirm`
Verifies OTP and marks email as verified.

Request:
```json
{
  "email": "user@example.com",
  "code": "123456"
}
```

### `PATCH /auth/users/{id}/password`
Changes user password and revokes refresh tokens.

Request:
```json
{
  "newPassword": "new-strong-password"
}
```

## Data Model

**UserAuth**
- `email`, `password`, `emailVerified`, `accountLocked`
- Extends `BaseEntity` for auditing.

**RefreshToken**
- Stores refresh token with expiry and revocation status.

**EmailOtp**
- Stores hashed OTP, expiry, attempts, cooldowns.

**AuditLog**
- Stores audit trail for all important actions.

## Security

- Stateless sessions.
- JWT Bearer tokens for protected endpoints.
- Argon2 password hashing.
- Email OTP for email verification.
- Rate limiting per IP + method + path.

## Database & Migrations

Flyway migrations:
- `V1__create_user_auth_table.sql`
- `V2__create_audit_table_logs.sql`
- `V3__create_user_auth_triggers.sql` (MySQL-only location)
- `V4__alter_audit_logs_nullable_entity_id.sql`
- `V5__create_refresh_tokens_table.sql`
- `V6__alter_audit_logs_action_length.sql`
- `V7__create_email_otp_table.sql`

## Configuration

Database:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_SCHEMA`

JWT:
- `JWT_SECRET`
- `JWT_ISSUER`
- `JWT_ACCESS_TOKEN_EXPIRATION_MS`
- `JWT_REFRESH_TOKEN_EXPIRATION_MS`

SMTP / Email OTP:
- `SMTP_HOST`
- `SMTP_PORT`
- `SMTP_USERNAME`
- `SMTP_PASSWORD`
- `SMTP_FROM`
- `SMTP_USE_TLS`
- `OTP_LENGTH`
- `OTP_EXPIRY_MINUTES`
- `OTP_MAX_SEND_PER_HOUR`
- `OTP_MAX_VERIFY_ATTEMPTS`
- `OTP_RESEND_COOLDOWN_SECONDS`

Rate limiting:
- `RATE_LIMIT_REQUESTS`
- `RATE_LIMIT_WINDOW_SECONDS`

## Local Run

From `Auth/`:
```
mvn clean spring-boot:run
```

## When To Use This Service

Use when:
- You need email+password auth with JWT.
- You want a centralized auth microservice.
- You need audit logs for security events.

Avoid when:
- You need OAuth/OIDC or social logins.
- You need MFA beyond email OTP.
- You need a full user profile service. This focuses on auth only.
