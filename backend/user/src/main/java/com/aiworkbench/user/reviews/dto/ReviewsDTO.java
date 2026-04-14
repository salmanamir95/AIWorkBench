package com.aiworkbench.user.reviews.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewsDTO {

    private Long userId;
    private Long raterId;
    private float rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
