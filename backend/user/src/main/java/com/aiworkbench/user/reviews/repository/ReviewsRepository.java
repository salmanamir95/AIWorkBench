package com.aiworkbench.user.reviews.repository;

import com.aiworkbench.user.reviews.entity.Reviews;
import com.aiworkbench.user.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewsRepository extends JpaRepository<Reviews, String> {

    // ✅ Basic finders
    List<Reviews> findByUser(Users user);
    List<Reviews> findByRater(Users rater);

    Optional<Reviews> findByUserAndRater(Users user, Users rater);

    boolean existsByUserAndRater(Users user, Users rater);

    void deleteByUserAndRater(Users user, Users rater);

    // ✅ Count
    long countByUser(Users user);
    long countByRater(Users rater);

    // ✅ Average rating
    @Query("SELECT AVG(r.rating) FROM Reviews r WHERE r.user = :user")
    Double getAverageRatingByUser(Users user);

    // ✅ Rating filters
    List<Reviews> findByUserAndRatingGreaterThanEqual(Users user, float rating);

    List<Reviews> findByUserAndRatingLessThanEqual(Users user, float rating);

    List<Reviews> findByRatingBetween(float min, float max);

    // ✅ Top reviewers (custom)
    @Query("SELECT r.rater, COUNT(r) as total FROM Reviews r GROUP BY r.rater ORDER BY total DESC")
    List<Object[]> findTopRaters();

    // ✅ Latest reviews
    List<Reviews> findTop10ByUserOrderByCreatedAtDesc(Users user);

    List<Reviews> findTop10ByRaterOrderByCreatedAtDesc(Users rater);

    // ✅ Get all ratings given by a user
    @Query("SELECT r.rating FROM Reviews r WHERE r.rater = :rater")
    List<Float> getRatingsGivenByUser(Users rater);

    // ✅ Get all ratings received by a user
    @Query("SELECT r.rating FROM Reviews r WHERE r.user = :user")
    List<Float> getRatingsReceivedByUser(Users user);
}
