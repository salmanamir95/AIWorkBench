package com.example.Auth.db.models;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.example.Auth.db.audit.EntityAuditListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, EntityAuditListener.class})
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    protected Instant updatedAt;

    @CreatedBy
    @Column(updatable = false, length = 100)
    protected String createdBy;

    @LastModifiedBy
    @Column(length = 100)
    protected String lastModifiedBy;

    public Instant getCreatedAt()        { return createdAt; }
    public Instant getUpdatedAt()        { return updatedAt; }
    public String getCreatedBy()         { return createdBy; }
    public String getLastModifiedBy()    { return lastModifiedBy; }

    public abstract Long getId();
}
