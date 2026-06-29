package com.aiworkbench.user.controllers;

import com.aiworkbench.user.dtos.UserReviewDTO;
import com.aiworkbench.user.services.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ur") // Matches /user/ur after Gateway stripPrefix(1)
@RequiredArgsConstructor
public class UserReviewController {

    private final UserReviewService reviewService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReviewDTO>> getReviewsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsForUser(userId));
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<List<UserReviewDTO>> getReviewsByReviewer(@PathVariable Long reviewerId) {
        return ResponseEntity.ok(reviewService.getReviewsByReviewer(reviewerId));
    }

    @PostMapping
    public ResponseEntity<UserReviewDTO> createReview(@RequestBody UserReviewDTO dto) {
        try {
            return ResponseEntity.ok(reviewService.createReview(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}