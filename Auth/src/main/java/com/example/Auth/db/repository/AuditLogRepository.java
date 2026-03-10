package com.example.Auth.db.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Auth.db.models.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByEntityNameAndEntityId(String entityName, Long entityId);
    List<AuditLog> findByPerformedBy(String performedBy);
    List<AuditLog> findByAction(AuditLog.AuditAction action);
    List<AuditLog> findByCreatedAtBetween(Instant from, Instant to);
}