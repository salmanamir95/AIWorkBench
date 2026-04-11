package com.aiworkbench.user.user.entity;

import java.util.Date;

import com.aiworkbench.user.audit.Audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Entity
@Table(
    name="users",
    indexes = {
        @Index(name = "idx_users_name", columnList = "name")
    }
)
public class Users extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "auth_id", nullable = false, unique=true, length=36)
    private String auth_id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "dob", nullable = false)
    private Date dob;
    
}
