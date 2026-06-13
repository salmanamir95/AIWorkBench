package com.aiworkbench.user.user.entity;

import java.time.LocalDate;

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
        @Index(name = "idx_users_name", columnList = "name"),
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_username", columnList = "username")
    }
)
public class Users extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "dob")
    private LocalDate dob;

    @Builder.Default
    @Column(name = "is_verified", nullable = false)
    private Boolean verified = false;
    
}
