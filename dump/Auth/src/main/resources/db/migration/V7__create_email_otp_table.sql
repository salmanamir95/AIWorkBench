CREATE TABLE email_otp (
    id                 BIGINT          NOT NULL AUTO_INCREMENT,
    email              VARCHAR(150)    NOT NULL,
    code_hash          VARCHAR(255)    NOT NULL,
    expires_at         DATETIME(6)     NOT NULL,
    attempts           INT             NOT NULL DEFAULT 0,
    max_attempts       INT             NOT NULL DEFAULT 5,
    sent_count         INT             NOT NULL DEFAULT 0,
    last_sent_at       DATETIME(6),
    verified           BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at         DATETIME(6)     NOT NULL,
    updated_at         DATETIME(6)     NOT NULL,
    created_by         VARCHAR(100),
    last_modified_by   VARCHAR(100),

    PRIMARY KEY (id),

    INDEX idx_email_otp_email (email),
    INDEX idx_email_otp_expires (expires_at),
    INDEX idx_email_otp_verified (verified)
);
