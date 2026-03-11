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
- **Encryption**: Key management, encryption policies, decrypt approvals.
- **Tools**: Tool catalog, tool configs, tool execution metadata.
- **Notification**: Email, SMS, in-app notifications, templates.
- **Audit**: Centralized security and compliance logs.
- **Search**: Indexing and semantic search across assets and tasks.
- **Integration**: Webhooks, third-party connectors, inbound events.
- **Billing**: Plans, quotas, usage metering.
- **Analytics**: Usage analytics, dashboards, KPIs.

## Data Ownership

- Auth owns credentials, refresh tokens, OTPs.
- User owns profile data, preferences.
- Team owns org structures and membership.
- Workflow owns workflow states and approvals.
- Asset owns asset metadata and lineage.
- Encryption owns keys, policies, and rotation history.
- Audit stores immutable security events.

## Phase 1 — Core Platform

**Scope**
- Auth service and Gateway.
- User and Team services.
- Basic audit logging.

**Deliverable**
- Working authentication, user profiles, team membership, and secure API entry.

## Phase 2 — Collaboration Backbone

**Scope**
- Workflow service.
- Asset service (metadata only).
- Notification service.

**Deliverable**
- End-to-end workflow execution on asset metadata with notifications.

## Phase 3 — Security & Compliance

**Scope**
- Encryption service with key management.
- Audit service with immutable logs.
- Policy enforcement in Gateway and Workflow.

**Deliverable**
- Policy-driven workflows and auditable security events.

## Phase 4 — Productivity & Extensibility

**Scope**
- Tools service and Integration service.
- Search service for fast discovery.

**Deliverable**
- Tool-driven workflows, external integrations, and platform-wide search.

## Phase 5 — Scale & Intelligence

**Scope**
- Analytics service.
- Billing service.
- Performance, reliability, and SLOs.

**Deliverable**
- Usage visibility, monetization, and scalable operations.

## Cross-Cutting Platform Work

- CI/CD pipelines and environment promotion.
- Secrets management and config policy.
- Observability with logs, metrics, traces.
- Data retention, backup, and disaster recovery.
- Security scanning and dependency management.
