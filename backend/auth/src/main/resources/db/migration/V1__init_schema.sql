-- =====================================================================
-- AIWorkbench Auth Service
-- V1__init_schema.sql
--
-- Initial schema for:
--  - Internal Auth users
--  - OAuth2 Registered Clients
--  - OAuth2 Authorizations
--
-- PostgreSQL
-- =====================================================================

-- =====================================================================
-- USERS
-- =====================================================================

CREATE TABLE users (
    id UUID PRIMARY KEY,

    -- ID of the user in the User Service
    user_id BIGINT NOT NULL UNIQUE,

    email VARCHAR(255) NOT NULL UNIQUE,

    password_hash VARCHAR(255) NOT NULL,

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    deleted_at TIMESTAMPTZ,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_users_user_id
    ON users(user_id);

CREATE INDEX idx_users_enabled
    ON users(enabled);



-- =====================================================================
-- OAUTH2 REGISTERED CLIENT
-- (Spring Authorization Server)
-- =====================================================================

CREATE TABLE oauth2_registered_client (

    id VARCHAR(100) PRIMARY KEY,

    client_id VARCHAR(100) NOT NULL UNIQUE,

    client_id_issued_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    client_secret TEXT,

    client_secret_expires_at TIMESTAMPTZ,

    client_name VARCHAR(200) NOT NULL,

    client_authentication_methods TEXT NOT NULL,

    authorization_grant_types TEXT NOT NULL,

    redirect_uris TEXT,

    post_logout_redirect_uris TEXT,

    scopes TEXT NOT NULL,

    client_settings TEXT NOT NULL,

    token_settings TEXT NOT NULL
);



-- =====================================================================
-- OAUTH2 AUTHORIZATION
-- (Spring Authorization Server)
-- =====================================================================

CREATE TABLE oauth2_authorization (

    id VARCHAR(100) PRIMARY KEY,

    registered_client_id VARCHAR(100) NOT NULL,

    principal_name VARCHAR(200) NOT NULL,

    authorization_grant_type VARCHAR(100) NOT NULL,

    authorized_scopes TEXT,

    attributes TEXT,

    state VARCHAR(500),

    authorization_code_value TEXT,
    authorization_code_issued_at TIMESTAMPTZ,
    authorization_code_expires_at TIMESTAMPTZ,
    authorization_code_metadata TEXT,

    access_token_value TEXT,
    access_token_issued_at TIMESTAMPTZ,
    access_token_expires_at TIMESTAMPTZ,
    access_token_metadata TEXT,
    access_token_type VARCHAR(100),
    access_token_scopes TEXT,

    oidc_id_token_value TEXT,
    oidc_id_token_issued_at TIMESTAMPTZ,
    oidc_id_token_expires_at TIMESTAMPTZ,
    oidc_id_token_metadata TEXT,

    refresh_token_value TEXT,
    refresh_token_issued_at TIMESTAMPTZ,
    refresh_token_expires_at TIMESTAMPTZ,
    refresh_token_metadata TEXT,

    user_code_value TEXT,
    user_code_issued_at TIMESTAMPTZ,
    user_code_expires_at TIMESTAMPTZ,
    user_code_metadata TEXT,

    device_code_value TEXT,
    device_code_issued_at TIMESTAMPTZ,
    device_code_expires_at TIMESTAMPTZ,
    device_code_metadata TEXT,

    CONSTRAINT fk_authorization_registered_client
        FOREIGN KEY (registered_client_id)
        REFERENCES oauth2_registered_client(id)
        ON DELETE CASCADE
);



-- =====================================================================
-- INDEXES
-- =====================================================================

CREATE INDEX idx_oauth2_registered_client_client_id
    ON oauth2_registered_client(client_id);

CREATE INDEX idx_oauth2_authorization_registered_client
    ON oauth2_authorization(registered_client_id);

CREATE INDEX idx_oauth2_authorization_principal
    ON oauth2_authorization(principal_name);

CREATE INDEX idx_oauth2_authorization_state
    ON oauth2_authorization(state);