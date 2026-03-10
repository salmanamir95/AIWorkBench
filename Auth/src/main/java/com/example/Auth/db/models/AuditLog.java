package com.example.Auth.db.models;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "audit_logs",
    indexes = {
        @Index(name = "idx_audit_entity",      columnList = "entityName"),
        @Index(name = "idx_audit_action",      columnList = "action"),
        @Index(name = "idx_audit_performed_by", columnList = "performedBy"),
        @Index(name = "idx_audit_created_at",  columnList = "createdAt")
    }
)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String entityName;          // e.g. "UserInfo"

    @Column(nullable = false)
    private Long entityId;              // e.g. 1

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuditAction action;         // CREATED, UPDATED, DELETED

    @Column(length = 150)
    private String performedBy;         // email of who did it

    @Column(nullable = false)
    private Instant createdAt;          // when it happened

    @Column(columnDefinition = "TEXT")
    private String details;             // optional extra info

    // ---------- ENUM ----------

    public enum AuditAction {
        CREATED, UPDATED, DELETED
    }

    // ---------- CONSTRUCTORS ----------

    protected AuditLog() {}

    public AuditLog(
        final String entityName,
        final Long entityId,
        final AuditAction action,
        final String performedBy,
        final String details
    ) {
        this.entityName  = entityName;
        this.entityId    = entityId;
        this.action      = action;
        this.performedBy = performedBy;
        this.createdAt   = Instant.now();
        this.details     = details;
    }

    // ---------- GETTERS ----------

    public Long getId()              { return id; }
    public String getEntityName()    { return entityName; }
    public Long getEntityId()        { return entityId; }
    public AuditAction getAction()   { return action; }
    public String getPerformedBy()   { return performedBy; }
    public Instant getCreatedAt()    { return createdAt; }
    public String getDetails()       { return details; }
}