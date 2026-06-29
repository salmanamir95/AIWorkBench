package com.aiworkbench.user.mappers;

import com.aiworkbench.user.dtos.UserReviewDTO;
import com.aiworkbench.user.entities.UserReviews;
import com.aiworkbench.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReviewMapper {

    private final UserRepository userRepository;

    public UserReviewDTO toDTO(UserReviews entity) {
        if (entity == null) return null;

        return UserReviewDTO.builder()
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .reviewerId(entity.getReviewer() != null ? entity.getReviewer().getId() : null)
                .rating(entity.getRating())
                .comment(entity.getComment())
                .reviewDate(entity.getReviewDate())
                .build();
    }

    public UserReviews toEntity(UserReviewDTO dto) {
        if (dto == null) return null;

        UserReviews.UserReviewsBuilder builder = UserReviews.builder()
                .rating(dto.rating())
                .comment(dto.comment())
                .reviewDate(dto.reviewDate());

        // Resolve the "User being reviewed"
        if (dto.userId() != null) {
            userRepository.findById(dto.userId())
                    .ifPresent(builder::user);
        }

        // Resolve the "Reviewer"
        if (dto.reviewerId() != null) {
            userRepository.findById(dto.reviewerId())
                    .ifPresent(builder::reviewer);
        }

        return builder.build();
    }
}