package com.aiworkbench.user.userDashboard.mapper;

import java.math.BigDecimal;

import com.aiworkbench.user.compensation.dto.CompensationDTO;
import com.aiworkbench.user.user.dto.UserDTO;
import com.aiworkbench.user.userDashboard.dto.CompensationSummaryDTO;
import com.aiworkbench.user.userDashboard.dto.LeaderboardDTO;
import com.aiworkbench.user.userDashboard.dto.ReviewSummaryDTO;
import com.aiworkbench.user.userDashboard.dto.TopRaterDTO;
import com.aiworkbench.user.userDashboard.dto.UserAnalyticsDTO;
import com.aiworkbench.user.userDashboard.dto.UserDashboardDTO;
import com.aiworkbench.user.userDashboard.dto.UserSummaryCardDTO;

public class UserDashboardMapper {

    public static UserDashboardDTO toProfile(UserDTO user) {
        return UserDashboardDTO.builder()
                .authId(user.getAuthId())
                .name(user.getName())
                .dob(user.getDob())
                .build();
    }

    public static UserDashboardDTO toDashboard(UserDTO user,
                                               CompensationSummaryDTO compensation,
                                               ReviewSummaryDTO reviews) {
        return UserDashboardDTO.builder()
                .authId(user.getAuthId())
                .name(user.getName())
                .dob(user.getDob())
                .compensation(compensation)
                .reviews(reviews)
                .build();
    }

    public static UserDashboardDTO toDashboardWithReviews(UserDTO user, ReviewSummaryDTO reviews) {
        return UserDashboardDTO.builder()
                .authId(user.getAuthId())
                .name(user.getName())
                .dob(user.getDob())
                .reviews(reviews)
                .build();
    }

    public static UserDashboardDTO toDashboardWithCompensation(UserDTO user, CompensationSummaryDTO compensation) {
        return UserDashboardDTO.builder()
                .authId(user.getAuthId())
                .name(user.getName())
                .dob(user.getDob())
                .compensation(compensation)
                .build();
    }

    public static UserAnalyticsDTO toAnalytics(UserDTO user,
                                               Double averageRating,
                                               Long totalReviews,
                                               CompensationDTO current,
                                               BigDecimal salaryGrowth) {
        return UserAnalyticsDTO.builder()
                .name(user.getName())
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .currentSalary(current != null ? current.getSalary() : null)
                .currentBonus(current != null ? current.getBonus() : null)
                .currency(current != null ? current.getCurrency() : null)
                .salaryGrowth(salaryGrowth)
                .build();
    }

    public static UserSummaryCardDTO toSummaryCard(UserDTO user,
                                                   Double averageRating,
                                                   Long totalReviews,
                                                   BigDecimal currentSalary) {
        return UserSummaryCardDTO.builder()
                .name(user.getName())
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .currentSalary(currentSalary)
                .build();
    }

    public static LeaderboardDTO toLeaderboard(String name,
                                               Double averageRating,
                                               BigDecimal currentSalary,
                                               Double combinedScore) {
        return LeaderboardDTO.builder()
                .name(name)
                .averageRating(averageRating)
                .currentSalary(currentSalary)
                .combinedScore(combinedScore)
                .build();
    }

    public static TopRaterDTO toTopRater(String name, Long ratingsGiven) {
        return TopRaterDTO.builder()
                .name(name)
                .ratingsGiven(ratingsGiven)
                .build();
    }
}
