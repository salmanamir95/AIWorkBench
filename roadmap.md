# AIWorkbench Roadmap

This roadmap defines the platform as a set of focused microservices. Each service owns a single responsibility, its own data, and a clear API contract.

## Principles

- Single responsibility per service.
- Each service owns its data and schema.
- Contracts are explicit and versioned.
- Prefer async events for cross-service side effects.
- Security, audit, and observability are first-class.

## Microservices Overview

- **Gateway**: Entry point, routing, TLS termination, rate limits, auth enforcement.
- **Auth**: Identity, JWT issuance, refresh tokens, email verification, login audit.
- **User**: User profile and preferences, separate from auth.
- **Team**: Teams, memberships, roles, invitations.
- **Workflow**: Workflow definitions, steps, approvals, and state transitions.
- **Asset**: Asset metadata, ownership, classification, versioning.
- **Asset Sharing**: P2P sharing service for asset distribution and access.
- **Encryption**: Key management, encryption policies, decrypt approvals.
- **AI Service**: Unified endpoints for all AI capabilities and models.
- **Tools**: Tool catalog, tool configs, tool execution metadata.
- **Notification**: Email, SMS, in-app notifications, templates.
- **Audit**: Centralized security and compliance logs.
- **Search**: Indexing and semantic search across assets and tasks.
- **Integration**: Webhooks, third-party connectors, inbound events.
- **Billing**: Plans, quotas, usage metering.
- **Analytics**: Usage analytics, dashboards, KPIs.

## Microservice Modules (Security, Audit, Third-Party)

- **Gateway**: Security = TLS termination, WAF/rate limiting, JWT validation. Audit = request/deny logs. Third-party = WAF/CDN provider.
- **Auth**: Security = password hashing, MFA/OTP, token rotation. Audit = login/verification events. Third-party = email/SMS provider.
- **User**: Security = PII access controls. Audit = profile change logs. Third-party = none (internal only).
- **Team**: Security = role-based access control. Audit = membership/invite logs. Third-party = none (internal only).
- **Workflow**: Security = approval policies, role checks. Audit = state transition logs. Third-party = none (internal only).
- **Asset**: Security = ownership checks, classification. Audit = asset change logs. Third-party = object storage (if externalized).
- **Asset Sharing**: Security = P2P access controls, share tokens. Audit = share/accept/revoke logs. Third-party = none (internal only).
- **Encryption**: Security = key management, rotation, envelope encryption. Audit = key usage logs. Third-party = KMS/HSM provider.
- **AI Service**: Security = request auth, model access policies. Audit = model usage logs. Third-party = AI model providers.
- **Tools**: Security = execution sandboxing, permissions. Audit = tool run logs. Third-party = tool vendor APIs.
- **Notification**: Security = template validation, abuse controls. Audit = send/failed logs. Third-party = email/SMS/push provider.
- **Audit**: Security = immutability, retention controls. Audit = self-audit integrity checks. Third-party = long-term storage (optional).
- **Search**: Security = index ACLs. Audit = search query logs. Third-party = search engine (if externalized).
- **Integration**: Security = signature verification, secret vaulting. Audit = webhook delivery logs. Third-party = SaaS connectors.
- **Billing**: Security = PCI boundary, tokenized payments. Audit = invoice/charge logs. Third-party = payment provider.
- **Analytics**: Security = data minimization, access controls. Audit = report access logs. Third-party = BI/telemetry tools (optional).

## Data Ownership

- Auth owns credentials, refresh tokens, OTPs.
- User owns profile data, preferences.
- Team owns org structures and membership.
- Workflow owns workflow states and approvals.
- Asset owns asset metadata and lineage.
- Encryption owns keys, policies, and rotation history.
- Audit stores immutable security events.

## Phase 0 — Shipped

**Scope**
- Auth microservice (production-ready MVP).

**Deliverable**
- Working authentication with JWT access/refresh, OTP verification, audit logging, and rate limiting.

## Phase 1 — Core Platform (Next)

**Microservices**
- Gateway
- User
- Team
- Audit (basic)

**Integrations**
- Gateway ↔ Auth (JWT validation, rate limits, routing)
- User ↔ Auth (profile linkage)
- Team ↔ User (membership, roles)
- Audit ↔ Gateway/Auth (security events)

**Deliverable**
- Secure API entry with user profiles, team membership, and baseline audit logging.

## Phase 2 — Collaboration Backbone

**Microservices**
- Asset
- Asset Sharing (P2P)
- Workflow
- Notification

**Integrations**
- Workflow ↔ Asset (metadata state transitions)
- Asset Sharing ↔ Asset (ownership, access grants)
- Workflow ↔ Notification (approval and status events)
- Asset Sharing ↔ Notification (share invites, access changes)

**Deliverable**
- End-to-end asset workflow with P2P sharing and notifications.

## Phase 3 — Security & Compliance

**Microservices**
- Encryption
- Audit (immutable store + retention)

**Integrations**
- Gateway ↔ Encryption (policy enforcement)
- Workflow ↔ Encryption (approval-based decrypt)
- Asset ↔ Encryption (classified asset handling)
- All services → Audit (security/compliance events)

**Deliverable**
- Policy-driven workflows and auditable security events.

## Phase 4 — Productivity & Extensibility

**Microservices**
- AI Service
- Tools
- Integration
- Search

**Integrations**
- Tools ↔ AI Service (execution + model routing)
- Integration ↔ Tools/Workflow (external triggers, webhooks)
- Search ↔ Asset/Workflow (indexing + semantic search)

**Deliverable**
- Tool-driven workflows, external integrations, AI endpoints, and platform-wide search.

## Phase 5 — Scale & Intelligence

**Microservices**
- Analytics
- Billing

**Integrations**
- Billing ↔ Gateway (quotas, usage metering)
- Analytics ↔ All services (KPIs, dashboards)

**Deliverable**
- Usage visibility, monetization, and scalable operations.

## Cross-Cutting Platform Work

- CI/CD pipelines and environment promotion.
- Secrets management and config policy.
- Observability with logs, metrics, traces.
- Data retention, backup, and disaster recovery.
- Security scanning and dependency management.
