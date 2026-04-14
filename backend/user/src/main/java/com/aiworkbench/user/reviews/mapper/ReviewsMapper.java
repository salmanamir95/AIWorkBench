package com.aiworkbench.user.reviews.mapper;

import com.aiworkbench.user.reviews.dto.ReviewsDTO;
import com.aiworkbench.user.reviews.entity.Reviews;
import com.aiworkbench.user.user.entity.Users;

public class ReviewsMapper {

    public static ReviewsDTO toDTO(Reviews entity) {
        if (entity == null) return null;

        ReviewsDTO dto = new ReviewsDTO();
        dto.setUserId(entity.getUser().getId());
        dto.setRaterId(entity.getRater().getId());
        dto.setRating(entity.getRating());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Reviews toEntity(ReviewsDTO dto, Users user, Users rater) {
        if (dto == null) return null;

        Reviews entity = new Reviews();
        entity.setUser(user);
        entity.setRater(rater);
        entity.setRating(dto.getRating());
        return entity;
    }
}
