CREATE TABLE user_auth (
    id                 BIGINT          NOT NULL AUTO_INCREMENT,
    email              VARCHAR(150)    NOT NULL,
    password           VARCHAR(255)    NOT NULL,
    email_verified     BOOLEAN         NOT NULL DEFAULT FALSE,
    account_locked     BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at         DATETIME(6)     NOT NULL,
    updated_at         DATETIME(6)     NOT NULL,
    created_by         VARCHAR(100),
    last_modified_by   VARCHAR(100),

    PRIMARY KEY (id),

    CONSTRAINT uk_user_auth_email UNIQUE (email),

    INDEX idx_user_auth_email            (email),
    INDEX idx_user_auth_created_at       (created_at),
    INDEX idx_user_auth_email_verified   (email_verified),
    INDEX idx_user_auth_account_locked   (account_locked),
    INDEX idx_user_auth_verified_created (email_verified, created_at)
);
