package com.aiworkbench.user.repositories;

import java.util.List;

import org.hibernate.annotations.processing.Find;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aiworkbench.user.entities.UserReviews;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReviews, Long> {

    // Find all reviews received by a specific user
    List<UserReviews> findByUserId(Long userId);

    // Find all reviews written by a specific reviewer
    List<UserReviews> findByReviewerId(Long reviewerId);

    // Find reviews for a user with a specific minimum rating
    List<UserReviews> findByUserIdAndRatingGreaterThanEqual(Long userId, Integer rating);
}
