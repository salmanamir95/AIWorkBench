package com.aiworkbench.user.userDashboard.dto;

import java.math.BigDecimal;

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
public class UserSummaryCardDTO {

    private Long userId;
    private String name;
    private Double averageRating;
    private Long totalReviews;
    private BigDecimal currentSalary;
}
