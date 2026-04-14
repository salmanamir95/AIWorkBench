CREATE TABLE project_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    visibility VARCHAR(10) NOT NULL,
    allow_guest_access BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_project_settings_project
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE INDEX idx_project_settings_project (project_id)
) ENGINE=InnoDB;
