package com.aiworkbench.user.reviews.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiworkbench.user.gr.GR;
import com.aiworkbench.user.reviews.dto.ReviewsDTO;
import com.aiworkbench.user.reviews.service.ReviewsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewsService reviewsService;

    // CREATE review
    @PostMapping
    public ResponseEntity<GR<ReviewsDTO>> create(@RequestBody ReviewsDTO dto) {
        ReviewsDTO created = reviewsService.createReview(dto);
        return ResponseEntity.ok(GR.success(created, "Review created successfully"));
    }

    // UPDATE review (by user + rater)
    @PutMapping
    public ResponseEntity<GR<ReviewsDTO>> update(@RequestBody ReviewsDTO dto) {
        ReviewsDTO updated = reviewsService.updateReview(dto);
        return ResponseEntity.ok(GR.success(updated, "Review updated successfully"));
    }

    // DELETE review (by user + rater)
    @DeleteMapping
    public ResponseEntity<GR<Void>> delete(
            @RequestParam Long userId,
            @RequestParam Long raterId
    ) {
        reviewsService.deleteReview(toPairDto(userId, raterId));
        return ResponseEntity.ok(GR.success(null, "Review deleted successfully"));
    }

    // GET review (by user + rater)
    @GetMapping
    public ResponseEntity<GR<ReviewsDTO>> get(
            @RequestParam Long userId,
            @RequestParam Long raterId
    ) {
        ReviewsDTO review = reviewsService.getReview(toPairDto(userId, raterId));
        return ResponseEntity.ok(GR.success(review, "Review fetched successfully"));
    }

    // GET all reviews for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<GR<List<ReviewsDTO>>> getByUser(@PathVariable Long userId) {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setUserId(userId);
        List<ReviewsDTO> reviews = reviewsService.getReviewsForUser(dto);
        return ResponseEntity.ok(GR.success(reviews, "User reviews fetched successfully"));
    }

    // GET all reviews by rater
    @GetMapping("/rater/{raterId}")
    public ResponseEntity<GR<List<ReviewsDTO>>> getByRater(@PathVariable Long raterId) {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setRaterId(raterId);
        List<ReviewsDTO> reviews = reviewsService.getReviewsByRater(dto);
        return ResponseEntity.ok(GR.success(reviews, "Rater reviews fetched successfully"));
    }

    // GET top recent reviews for a user
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<GR<List<ReviewsDTO>>> getRecentByUser(@PathVariable Long userId) {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setUserId(userId);
        List<ReviewsDTO> reviews = reviewsService.getTopRecentReviews(dto);
        return ResponseEntity.ok(GR.success(reviews, "Recent reviews fetched successfully"));
    }

    private ReviewsDTO toPairDto(Long userId, Long raterId) {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setUserId(userId);
        dto.setRaterId(raterId);
        return dto;
    }
}
