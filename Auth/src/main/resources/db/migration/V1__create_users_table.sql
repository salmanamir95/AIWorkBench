CREATE TABLE users (
    id                BIGINT          NOT NULL AUTO_INCREMENT,
    name              VARCHAR(100)    NOT NULL,
    email             VARCHAR(150)    NOT NULL,
    password          VARCHAR(255)    NOT NULL,
    age               INT             NOT NULL,
    email_verified    BOOLEAN         NOT NULL DEFAULT FALSE,
    account_locked    BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at        DATETIME(6)     NOT NULL,
    updated_at        DATETIME(6)     NOT NULL,
    created_by        VARCHAR(100),
    last_modified_by  VARCHAR(100),

    PRIMARY KEY (id),

    CONSTRAINT uk_user_email UNIQUE (email),

    INDEX idx_user_email           (email),
    INDEX idx_user_created_at      (created_at),
    INDEX idx_user_email_verified  (email_verified),
    INDEX idx_user_account_locked  (account_locked),
    INDEX idx_user_verified_created (email_verified, created_at)
);