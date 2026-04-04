CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,

    is_verified BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_username UNIQUE (username),
    CONSTRAINT uq_email UNIQUE (email),

    CONSTRAINT chk_username_format
        CHECK (username REGEXP '^[A-Za-z][A-Za-z0-9_]{2,49}$'),

    CONSTRAINT chk_email_format
        CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')
)
ENGINE=InnoDB;