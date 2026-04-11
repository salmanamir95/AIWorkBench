CREATE TABLE compensation (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    salary DECIMAL(12,2) NOT NULL,
    bonus DECIMAL(12,2) DEFAULT 0,
    currency CHAR(3) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comp_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,          -- comma goes here (after CASCADE), not after ON UPDATE CASCADE
    CONSTRAINT chk_salary_positive CHECK (salary > 0),
    CONSTRAINT chk_bonus_non_negative CHECK (bonus >= 0),
    CONSTRAINT chk_date_range CHECK (effective_to IS NULL OR effective_to > effective_from)

     INDEX idx_comp_user_dates (user_id, effective_from, effective_to),
    INDEX idx_comp_currency_from (currency, effective_from)  -- optional: payroll reporting

) ENGINE=InnoDB;