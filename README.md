# AIWorkBench

AIWorkBench is a Java/Spring Boot microservices platform focused on secure collaboration, asset workflows, and enterprise-grade governance. Each service owns a single responsibility and a dedicated database schema.

## Architecture Overview

```
Client → API Gateway → Microservices → Datastores
                      ↘→ SMTP / External Providers
```

**Key principles**
- Single responsibility per service.
- Data ownership per service.
- Explicit and versioned API contracts.
- Observability, audit, and security by default.

## Services (Current + Planned)

- **Gateway**: Routing, TLS termination, auth enforcement, rate limits.
- **Auth**: Identity, JWT issuance, refresh tokens, email verification.
- **User**: User profiles, preferences (separate from auth).
- **Team**: Teams, memberships, roles, invitations.
- **Workflow**: Workflow definitions, approvals, transitions.
- **Asset**: Asset metadata, ownership, classification, versioning.
- **Encryption**: Key management, encryption policies, approvals.
- **Tools**: Tool catalog and execution metadata.
- **Notification**: Email/SMS/in-app notifications.
- **Audit**: Centralized immutable security logs.
- **Search**: Indexing and semantic search.
- **Integration**: Webhooks and third-party connectors.
- **Billing**: Plans, quotas, usage metering.
- **Analytics**: Usage analytics and KPIs.

## Current Implementation

**Auth Service (production-ready MVP)**
- Email/password authentication.
- JWT access and refresh tokens.
- Refresh token rotation and revocation.
- Email OTP verification.
- Audit logging for security events.
- Rate limiting per IP + route.

Auth docs: `Auth/docs/README.md`

## Local Development

### Prerequisites

- Java 25 (Temurin)
- Maven 3.9+
- Docker + Docker Compose v2
- MySQL 8.0 (optional if running only via Docker)

### Run Auth Service (local JVM)

From `Auth/`:
```
mvn clean spring-boot:run
```

### Run With Docker Compose (recommended)

From repo root:
```
docker compose up --build
```

Stop:
```
docker compose down
```

Ports:
- API: `http://localhost:8080`
- MySQL: `localhost:3307` (container port `3306`)

Swagger UI:
- `http://localhost:8080/swagger-ui/index.html`

## Configuration

Auth service uses environment variables (loaded via `Auth/.env`):

**Database**
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_SCHEMA`

**JWT**
- `JWT_SECRET`
- `JWT_ISSUER`
- `JWT_ACCESS_TOKEN_EXPIRATION_MS`
- `JWT_REFRESH_TOKEN_EXPIRATION_MS`

**SMTP / Email OTP**
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

**Rate Limiting**
- `RATE_LIMIT_REQUESTS`
- `RATE_LIMIT_WINDOW_SECONDS`

## Repository Layout

- `Auth/` Auth microservice (Spring Boot)
- `Auth/docs/README.md` Auth service details and API documentation
- `docker-compose.yml` Local multi-container setup
- `roadmap.md` Platform roadmap and service responsibilities

## Operational Notes

- The database uses Flyway migrations.
- Auth service logs audit events for security operations.
- The Auth service exposes HTTP only; TLS is expected at the gateway.

## Roadmap

See `roadmap.md` for the full microservice plan and delivery phases.
