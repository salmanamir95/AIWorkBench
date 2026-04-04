CREATE TABLE refresh_tokens (
    id                 BIGINT          NOT NULL AUTO_INCREMENT,
    user_id            BIGINT          NOT NULL,
    token              VARCHAR(512)    NOT NULL,
    expires_at         DATETIME(6)     NOT NULL,
    revoked            BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at         DATETIME(6)     NOT NULL,
    updated_at         DATETIME(6)     NOT NULL,
    created_by         VARCHAR(100),
    last_modified_by   VARCHAR(100),

    PRIMARY KEY (id),

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id) REFERENCES user_auth(id)
        ON DELETE CASCADE,

    UNIQUE KEY uk_refresh_token (token),
    INDEX idx_refresh_token_user (user_id),
    INDEX idx_refresh_token_expires (expires_at),
    INDEX idx_refresh_token_revoked (revoked)
);
