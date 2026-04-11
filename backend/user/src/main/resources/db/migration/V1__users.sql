CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    auth_id CHAR(36) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL
    
    INDEX idx_users_name (name)          -- add only if you query by name
) ENGINE=InnoDB;
