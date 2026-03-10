package com.example.Auth.db.audit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Auth.db.models.AuditLog;
import com.example.Auth.db.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(final AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // Use caller transaction so audit trail reflects committed business changes.
    @Transactional
    public void log(
        final String entityName,
        final Long entityId,
        final AuditLog.AuditAction action,
        final String performedBy,
        final String details
    ) {
        auditLogRepository.save(
            new AuditLog(entityName, entityId, action, performedBy, details)
        );
    }
}
