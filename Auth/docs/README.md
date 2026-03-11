# Auth Service Documentation

This document describes the current **Auth** service implementation: what it does, how it works, and how/when to use it. The service is intentionally **non‑JWT**. It provides signup, login, and password change flows using database‑backed credentials and Argon2 password hashing.

## What This Service Is For

Use this service when you need:
- **Account creation** with hashed password storage.
- **Credential verification** (email + password) without JWT issuance.
- **Account state checks** (locked, email verified).
- **Audit logging** of entity changes.

It is **not** responsible for:
- JWT access/refresh token creation or validation.
- Authorization beyond basic authenticated/unauthenticated separation.
- Social login or external identity providers.

## Where It’s Used

It is designed as a standalone auth module that can be called by:
- A web or mobile frontend for signup/login/password change.
- Other services in a monolith or microservice setup that need credential verification.

Base path for endpoints:
- `/auth`

OpenAPI / Swagger UI:
- `/swagger-ui.html`

## How It Works (Current Flow)

### Signup
1. `AuthController` receives request.
2. `UserAuthService.registerUser()` checks for existing email.
3. Password is hashed using **Argon2**.
4. `UserAuthRepository` persists the new user.

### Login
1. `AuthController` receives credentials.
2. `AuthenticationManager` orchestrates the flow:
   - `AuthenticationProvider` loads user and verifies password.
   - `AuthenticationVerifier` enforces account rules (locked, email verified).
3. Returns sanitized `UserCredentials` (no password).

### Change Password
1. `AuthController` receives userId + new password.
2. `UserAuthService.changePassword()` hashes and saves the new password.

## API Endpoints

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
    "id": 1,
    "email": "user@example.com",
    "emailVerified": false,
    "accountLocked": false,
    "createdAt": "2026-03-11T00:00:00Z",
    "updatedAt": "2026-03-11T00:00:00Z"
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
    "id": 1,
    "email": "user@example.com",
    "emailVerified": false,
    "accountLocked": false,
    "createdAt": "2026-03-11T00:00:00Z",
    "updatedAt": "2026-03-11T00:00:00Z"
  },
  "msg": "Login successful"
}
```

### `PATCH /auth/users/{id}/password`
Changes the password for a specific user.

Request:
```json
{
  "newPassword": "new-strong-password"
}
```

Response:
```json
{
  "success": true,
  "data": null,
  "msg": "Password changed successfully"
}
```

## Data Model

### `UserAuth` (Primary auth entity)
Fields:
- `id`
- `email`
- `password` (Argon2 hash, never returned in API)
- `emailVerified`
- `accountLocked`
- Auditing fields from `BaseEntity` (`createdAt`, `updatedAt`, etc.)

Rules:
- Passwords **must be hashed** before persisting.
- Email format is validated.

### Auditing
All entities extending `BaseEntity` are audited using `EntityAuditListener`.  
Audit logs are written to the `audit_log` table via `AuditLogService`.

## Security

Configured in `SecurityConfig`:
- Stateless sessions.
- CSRF, form login, logout disabled.
- `/auth/**` is public.
- All other endpoints require authentication.

Note: `InMemoryUserDetailsManager` is used as a placeholder to prevent default generated credentials. Replace it when integrating a real authenticated area.

## Database & Migrations

Uses **Flyway** for schema migrations:
- `V1__create_users_table.sql`
- `V2__create_audit_table_logs.sql`
- `V3__create_users_table_triggers.sql`

Production DB settings are configured via `application.properties` and `.env`:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_SCHEMA`

Tests use H2 (`application-test.properties`).

## Local Run

From `Auth/`:
```
mvn clean spring-boot:run
```

## When To Use This Project

Use this Auth service when:
- You want a basic, secure **email+password** auth flow.
- You **do not** want JWT token issuance.
- You need persistence + audit trails for user auth records.

Do **not** use it for:
- OAuth/OIDC flows.
- Multi‑factor authentication (not implemented).
- Token‑based session management.

