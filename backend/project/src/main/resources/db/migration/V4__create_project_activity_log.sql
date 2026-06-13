CREATE TABLE project_activity_log (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_activity_project
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_project_activity_project ON project_activity_log (project_id);
CREATE INDEX idx_project_activity_user ON project_activity_log (user_id);
