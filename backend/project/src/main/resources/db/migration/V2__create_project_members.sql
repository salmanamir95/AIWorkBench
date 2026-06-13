CREATE TABLE project_members (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    project_role VARCHAR(20) NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_members_project
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE UNIQUE INDEX idx_project_members_project_user ON project_members (project_id, user_id);
CREATE INDEX idx_project_members_user ON project_members (user_id);
CREATE INDEX idx_project_members_role ON project_members (project_role);
