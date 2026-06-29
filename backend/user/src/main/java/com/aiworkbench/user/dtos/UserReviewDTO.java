package com.aiworkbench.user.dtos;

import java.time.LocalDate;
import lombok.Builder;

/**
 * DTO for User Review information.
 * Uses userId for the employee being reviewed and reviewerId for the person writing it.
 */
@Builder
public record UserReviewDTO(
    Long userId,
    Long reviewerId,
    Integer rating,
    String comment,
    LocalDate reviewDate
) {}