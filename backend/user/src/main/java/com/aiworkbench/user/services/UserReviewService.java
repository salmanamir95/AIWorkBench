package com.aiworkbench.user.services;
import com.aiworkbench.user.dtos.UserReviewDTO;
import com.aiworkbench.user.entities.UserReviews;
import com.aiworkbench.user.mappers.UserReviewMapper;
import com.aiworkbench.user.repositories.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReviewService {

    private final UserReviewRepository reviewRepository;
    private final UserReviewMapper reviewMapper;

    public List<UserReviewDTO> getReviewsForUser(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    public List<UserReviewDTO> getReviewsByReviewer(Long reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId).stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    @Transactional
    public UserReviewDTO createReview(UserReviewDTO dto) {
        // Business Rule: A user cannot review themselves
        if (dto.userId().equals(dto.reviewerId())) {
            throw new IllegalArgumentException("Users cannot submit reviews for themselves.");
        }

        UserReviews entity = reviewMapper.toEntity(dto);
        return reviewMapper.toDTO(reviewRepository.save(entity));
    }

    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }
}
