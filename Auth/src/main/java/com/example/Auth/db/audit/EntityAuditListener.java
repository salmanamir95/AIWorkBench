package com.example.Auth.db.audit;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Auth.db.models.AuditLog;
import com.example.Auth.db.models.BaseEntity;
import com.example.Auth.db.config.SpringContextProvider;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class EntityAuditListener {

    private static final Logger log = LoggerFactory.getLogger(EntityAuditListener.class);

    @PostPersist
    public void onPostPersist(final Object entity) {
        if (entity instanceof BaseEntity base) {
            log.info("[AUDIT] CREATED | entity={} | createdBy={} | createdAt={}",
                    entity.getClass().getSimpleName(), base.getCreatedBy(), base.getCreatedAt());

            final Long entityId = base.getId();
            final AuditLogService auditLogService = SpringContextProvider.getBean(AuditLogService.class);
            if (auditLogService != null && entityId != null) {
                auditLogService.log(
                    entity.getClass().getSimpleName(),
                    entityId,
                    AuditLog.AuditAction.CREATED,
                    base.getCreatedBy(),
                    null
                );
            } else if (entityId == null) {
                log.warn("[AUDIT] Skipping CREATED log because entity id is null for {}",
                        entity.getClass().getSimpleName());
            }
        }
    }

    @PostUpdate
    public void onPostUpdate(final Object entity) {
        if (entity instanceof BaseEntity base) {
            log.info("[AUDIT] UPDATED | entity={} | modifiedBy={} | updatedAt={}",
                    entity.getClass().getSimpleName(), base.getLastModifiedBy(), base.getUpdatedAt());

            final Long entityId = base.getId();
            final AuditLogService auditLogService = SpringContextProvider.getBean(AuditLogService.class);
            if (auditLogService != null && entityId != null) {
                auditLogService.log(
                    entity.getClass().getSimpleName(),
                    entityId,
                    AuditLog.AuditAction.UPDATED,
                    base.getLastModifiedBy(),
                    null
                );
            } else if (entityId == null) {
                log.warn("[AUDIT] Skipping UPDATED log because entity id is null for {}",
                        entity.getClass().getSimpleName());
            }
        }
    }

    @PostRemove
    public void onPostRemove(final Object entity) {
        log.info("[AUDIT] DELETED | entity={} | deletedAt={}",
                entity.getClass().getSimpleName(), Instant.now());

        if (entity instanceof BaseEntity base) {
            final Long entityId = base.getId();
            final AuditLogService auditLogService = SpringContextProvider.getBean(AuditLogService.class);
            if (auditLogService != null && entityId != null) {
                auditLogService.log(
                    entity.getClass().getSimpleName(),
                    entityId,
                    AuditLog.AuditAction.DELETED,
                    base.getLastModifiedBy(),
                    null
                );
            } else if (entityId == null) {
                log.warn("[AUDIT] Skipping DELETED log because entity id is null for {}",
                        entity.getClass().getSimpleName());
            }
        }
    }
}
