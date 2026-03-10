CREATE TABLE audit_logs (
    id            BIGINT          NOT NULL AUTO_INCREMENT,
    entity_name   VARCHAR(100)    NOT NULL,
    entity_id     BIGINT          NOT NULL,
    action        VARCHAR(20)     NOT NULL,
    performed_by  VARCHAR(150),
    created_at    DATETIME(6)     NOT NULL,
    details       TEXT,

    PRIMARY KEY (id),

    INDEX idx_audit_entity       (entity_name),
    INDEX idx_audit_action       (action),
    INDEX idx_audit_performed_by (performed_by),
    INDEX idx_audit_created_at   (created_at)
);