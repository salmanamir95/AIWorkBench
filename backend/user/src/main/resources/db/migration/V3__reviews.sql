CREATE TABLE Reviews (
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    rater_id CHAR(36) NOT NULL,
    rating FLOAT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rater
        FOREIGN KEY (rater_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT rating_range
        CHECK (rating <= 5 AND rating >= 0) 

    UNIQUE INDEX idx_reviews_user_rater (user_id, rater_id),
    INDEX idx_reviews_rater_id (rater_id)
) ENGINE=InnoDB;
