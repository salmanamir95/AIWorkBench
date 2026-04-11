package com.aiworkbench.user.compensation.entity;

import com.aiworkbench.user.user.entity.Users;
import com.aiworkbench.user.audit.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "compensation",
    indexes = {
        @Index(name = "idx_comp_effective_range", columnList = "effective_from, effective_to"),
        @Index(name = "idx_comp_effective_range_user", columnList = "user_id, effective_from, effective_to"),
        @Index(name = "idx_comp_effective_from", columnList = "effective_from"),
        @Index(name = "idx_comp_effective_from_user", columnList = "user_id, effective_from"),
        @Index(name = "idx_comp_salary", columnList = "salary"),
        @Index(name = "idx_comp_bonus", columnList = "bonus")
    }
)
public class Compensation extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many compensations belong to one user
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal bonus = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;
}