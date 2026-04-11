CREATE TABLE Compensation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
        CHECK (effective_to IS NULL OR effective_to > effective_from),

    INDEX idx_comp_effective_range (effective_from, effective_to),
    INDEX idx_comp_effective_range_user (user_id, effective_from, effective_to),
    INDEX idx_comp_effective_from (effective_from),
    INDEX idx_comp_effective_from_user (user_id, effective_from),
    INDEX idx_comp_salary (salary),
    INDEX idx_comp_bonus (bonus)
) ENGINE=InnoDB;