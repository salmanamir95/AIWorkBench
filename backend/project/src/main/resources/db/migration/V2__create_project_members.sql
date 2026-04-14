CREATE TABLE project_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    project_role VARCHAR(20) NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_members_project
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE INDEX idx_project_members_project_user (project_id, user_id),
    INDEX idx_project_members_user (user_id),
    INDEX idx_project_members_role (project_role)
) ENGINE=InnoDB;
