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
public class SystemStatsDTO {

    private Long totalUsers;
    private Long totalReviews;
    private Double averageRating;
    private BigDecimal totalPayroll;
    private Long usersWithNoReviews;
    private Long usersWithNoCompensation;
}
