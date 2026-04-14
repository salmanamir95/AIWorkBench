package com.aiworkbench.user.reviews.service;

import com.aiworkbench.user.reviews.dto.ReviewsDTO;
import com.aiworkbench.user.reviews.entity.Reviews;
import com.aiworkbench.user.reviews.mapper.ReviewsMapper;
import com.aiworkbench.user.reviews.repository.ReviewsRepository;
import com.aiworkbench.user.user.entity.Users;
import com.aiworkbench.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewsService {

    private final ReviewsRepository reviewsRepository;
    private final UserRepository userRepository;

    // ✅ Create Review
    public ReviewsDTO createReview(ReviewsDTO dto) {
        Users user = getUserOrThrow(dto.getUserId());
        Users rater = getUserOrThrow(dto.getRaterId());

        if (user.equals(rater)) {
            throw new RuntimeException("User cannot rate themselves");
        }

        if (reviewsRepository.existsByUserAndRater(user, rater)) {
            throw new RuntimeException("Review already exists");
        }

        Reviews review = ReviewsMapper.toEntity(dto, user, rater);
        review.setId(UUID.randomUUID().toString());

        return ReviewsMapper.toDTO(reviewsRepository.save(review));
    }

    // ✅ Update Review
    public ReviewsDTO updateReview(ReviewsDTO dto) {
        Users user = getUserOrThrow(dto.getUserId());
        Users rater = getUserOrThrow(dto.getRaterId());

        Reviews review = reviewsRepository.findByUserAndRater(user, rater)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(dto.getRating());
        return ReviewsMapper.toDTO(reviewsRepository.save(review));
    }

    // ✅ Delete Review
    public void deleteReview(ReviewsDTO dto) {
        Users user = getUserOrThrow(dto.getUserId());
        Users rater = getUserOrThrow(dto.getRaterId());
        reviewsRepository.deleteByUserAndRater(user, rater);
    }

    // ✅ Get review
    public ReviewsDTO getReview(ReviewsDTO dto) {
        Users user = getUserOrThrow(dto.getUserId());
        Users rater = getUserOrThrow(dto.getRaterId());
        Reviews review = reviewsRepository.findByUserAndRater(user, rater)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return ReviewsMapper.toDTO(review);
    }

    // ✅ Get all reviews for a user
    public List<ReviewsDTO> getReviewsForUser(ReviewsDTO dto) {
        Users user = getUserOrThrow(dto.getUserId());
        return reviewsRepository.findByUser(user).stream()
                .map(ReviewsMapper::toDTO)
                .toList();
    }

    // ✅ Get all reviews by rater
    public List<ReviewsDTO> getReviewsByRater(ReviewsDTO dto) {
        Users rater = getUserOrThrow(dto.getRaterId());
        return reviewsRepository.findByRater(rater).stream()
                .map(ReviewsMapper::toDTO)
                .toList();
    }

    // ✅ Get top reviews
    public List<ReviewsDTO> getTopRecentReviews(ReviewsDTO dto) {
        Users user = getUserOrThrow(dto.getUserId());
        return reviewsRepository.findTop10ByUserOrderByCreatedAtDesc(user).stream()
                .map(ReviewsMapper::toDTO)
                .toList();
    }

    private Users getUserOrThrow(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User id is required");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
}
