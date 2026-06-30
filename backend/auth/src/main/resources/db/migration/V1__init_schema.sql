-- 1. User Credentials
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. OAuth2 Registered Clients (Required by Spring Auth Server)
CREATE TABLE oauth2_registered_client (
    id VARCHAR(255) PRIMARY KEY,
    client_id VARCHAR(255) UNIQUE NOT NULL,
    client_id_issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_secret VARCHAR(255) DEFAULT NULL,
    client_name VARCHAR(255) NOT NULL,
    client_authentication_methods VARCHAR(1000) NOT NULL,
    authorization_grant_types VARCHAR(1000) NOT NULL,
    redirect_uris VARCHAR(1000) DEFAULT NULL,
    scopes VARCHAR(1000) NOT NULL,
    client_settings VARCHAR(2000) NOT NULL,
    token_settings VARCHAR(2000) NOT NULL
);

-- 3. OAuth2 Authorization State
CREATE TABLE oauth2_authorization (
    id VARCHAR(255) PRIMARY KEY,
    registered_client_id VARCHAR(255) NOT NULL,
    principal_name VARCHAR(255) NOT NULL,
    authorization_grant_type VARCHAR(100) NOT NULL,
    authorized_scopes VARCHAR(1000) DEFAULT NULL,
    attributes TEXT DEFAULT NULL,
    state VARCHAR(500) DEFAULT NULL,
    authorization_code_value TEXT DEFAULT NULL,
    authorization_code_issued_at TIMESTAMP DEFAULT NULL,
    authorization_code_expires_at TIMESTAMP DEFAULT NULL,
    access_token_value TEXT DEFAULT NULL,
    access_token_issued_at TIMESTAMP DEFAULT NULL,
    access_token_expires_at TIMESTAMP DEFAULT NULL,
    refresh_token_value TEXT DEFAULT NULL,
    refresh_token_issued_at TIMESTAMP DEFAULT NULL,
    refresh_token_expires_at TIMESTAMP DEFAULT NULL
);