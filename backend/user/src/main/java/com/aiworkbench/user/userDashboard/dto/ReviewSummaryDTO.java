package com.aiworkbench.user.userDashboard.dto;

import java.util.List;

import com.aiworkbench.user.reviews.dto.ReviewsDTO;

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
public class ReviewSummaryDTO {

    private Double averageRating;
    private Long totalReviews;
    private List<Long> ratingDistribution;
    private List<ReviewsDTO> recentReviews;
}
