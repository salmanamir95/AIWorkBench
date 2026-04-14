package com.aiworkbench.user.reviews.entity;

import com.aiworkbench.user.audit.Audit;
import com.aiworkbench.user.user.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "reviews",
    indexes = {
        @Index(name = "idx_reviews_user_rater", columnList = "user_id, rater_id", unique = true),
        @Index(name = "idx_reviews_rater_id", columnList = "rater_id")
    }
)
public class Reviews extends Audit {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rater_id", nullable = false)
    private Users rater;

    @Column(nullable = false)
    private float rating;
}
