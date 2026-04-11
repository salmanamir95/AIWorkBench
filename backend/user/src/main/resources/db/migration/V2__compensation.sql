CREATE TABLE Compensation (
    id CHAR(36) PRIMARY KEY,
    user_id BIGINT NOT NULL,

    salary DECIMAL(12,2) NOT NULL,
    bonus DECIMAL(12,2) DEFAULT 0,

    currency CHAR(3) NOT NULL,

    effective_from DATE NOT NULL,
    effective_to DATE DEFAULT NULL,

    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_comp_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT chk_salary_positive
        CHECK (salary > 0),

    CONSTRAINT chk_bonus_non_negative
        CHECK (bonus >= 0),

    CONSTRAINT chk_date_range
        CHECK (effective_to IS NULL OR effective_to > effective_from)
) ENGINE=InnoDB;