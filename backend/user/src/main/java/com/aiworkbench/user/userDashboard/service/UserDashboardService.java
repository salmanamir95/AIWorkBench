package com.aiworkbench.user.userDashboard.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aiworkbench.user.compensation.dto.CompensationDTO;
import com.aiworkbench.user.compensation.service.CompensationService;
import com.aiworkbench.user.reviews.dto.ReviewsDTO;
import com.aiworkbench.user.reviews.service.ReviewsService;
import com.aiworkbench.user.user.dto.UserDTO;
import com.aiworkbench.user.user.entity.Users;
import com.aiworkbench.user.user.repository.UserRepository;
import com.aiworkbench.user.user.service.UserService;
import com.aiworkbench.user.userDashboard.dto.CompensationSummaryDTO;
import com.aiworkbench.user.userDashboard.dto.LeaderboardDTO;
import com.aiworkbench.user.userDashboard.dto.ReviewSummaryDTO;
import com.aiworkbench.user.userDashboard.dto.SystemStatsDTO;
import com.aiworkbench.user.userDashboard.dto.TopRaterDTO;
import com.aiworkbench.user.userDashboard.dto.UserAnalyticsDTO;
import com.aiworkbench.user.userDashboard.dto.UserDashboardDTO;
import com.aiworkbench.user.userDashboard.dto.UserSummaryCardDTO;
import com.aiworkbench.user.userDashboard.mapper.UserDashboardMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDashboardService {

    private final UserService userService;
    private final ReviewsService reviewsService;
    private final CompensationService compensationService;
    private final UserRepository userRepository;

    // ── Core Functions ────────────────────────────────────────────────────────

    public UserDashboardDTO getUserProfile(Long userId) {
        UserDTO user = userService.getByUserID(userId);
        return UserDashboardMapper.toProfile(user);
    }

    public UserDashboardDTO getFullUserDashboard(Long userId) {
        UserDTO user = userService.getByUserID(userId);
        return UserDashboardMapper.toDashboard(
                user,
                buildCompensationSummary(userId),
                buildReviewSummary(userId)
        );
    }

    public UserDashboardDTO getUserWithReviews(Long userId) {
        UserDTO user = userService.getByUserID(userId);
        return UserDashboardMapper.toDashboardWithReviews(user, buildReviewSummary(userId));
    }

    public UserDashboardDTO getUserWithCompensation(Long userId) {
        UserDTO user = userService.getByUserID(userId);
        return UserDashboardMapper.toDashboardWithCompensation(user, buildCompensationSummary(userId));
    }

    // ── Review Analytics ─────────────────────────────────────────────────────

    public Double getAverageRating(Long userId) {
        List<ReviewsDTO> reviews = getReviewsForUser(userId);
        return calculateAverageRating(reviews);
    }

    public List<Long> getRatingDistribution(Long userId) {
        List<ReviewsDTO> reviews = getReviewsForUser(userId);
        return calculateRatingDistribution(reviews);
    }

    public List<TopRaterDTO> getTopRaters() {
        List<Users> users = userRepository.findAll();
        List<TopRaterDTO> result = new ArrayList<>();

        for (Users user : users) {
            ReviewsDTO dto = new ReviewsDTO();
            dto.setRaterId(user.getId());
            List<ReviewsDTO> given = reviewsService.getReviewsByRater(dto);
            result.add(UserDashboardMapper.toTopRater(user.getName(), (long) given.size()));
        }

        result.sort(Comparator.comparing(TopRaterDTO::getRatingsGiven).reversed());
        return applyPage(result, Pageable.ofSize(10));
    }

    public List<ReviewsDTO> getRecentReviews(Long userId) {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setUserId(userId);
        return reviewsService.getTopRecentReviews(dto);
    }

    // ── Compensation Analytics ───────────────────────────────────────────────

    public CompensationDTO getCurrentCompensation(Long userId) {
        return compensationService.getCurrentByUser(userId);
    }

    public List<CompensationDTO> getCompensationHistory(Long userId) {
        return compensationService.getByUser(userId, Pageable.unpaged()).getContent();
    }

    public BigDecimal getSalaryGrowth(Long userId) {
        List<CompensationDTO> history = getCompensationHistory(userId);
        if (history.isEmpty()) {
            return BigDecimal.ZERO;
        }

        history.sort(Comparator.comparing(CompensationDTO::getEffectiveFrom));
        BigDecimal first = history.get(0).getSalary();
        BigDecimal last = history.get(history.size() - 1).getSalary();
        if (first == null || last == null) {
            return BigDecimal.ZERO;
        }
        return last.subtract(first);
    }

    public List<LeaderboardDTO> getHighestSalaryUsers() {
        return getHighestSalaryUsers(Pageable.ofSize(10));
    }

    public List<LeaderboardDTO> getHighestSalaryUsers(Pageable pageable) {
        List<Users> users = userRepository.findAll();
        List<LeaderboardDTO> result = new ArrayList<>();

        for (Users user : users) {
            try {
                CompensationDTO current = compensationService.getCurrentByUser(user.getId());
                result.add(UserDashboardMapper.toLeaderboard(
                        user.getName(),
                        null,
                        current.getSalary(),
                        null
                ));
            } catch (RuntimeException ignored) {
                // no current compensation
            }
        }

        result.sort(Comparator.comparing(LeaderboardDTO::getCurrentSalary, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        return applyPage(result, pageable);
    }

    // ── Combined Features ────────────────────────────────────────────────────

    public List<LeaderboardDTO> getTopUsersByRating() {
        return getTopUsersByRating(Pageable.ofSize(10));
    }

    public List<LeaderboardDTO> getTopUsersByRating(Pageable pageable) {
        List<Users> users = userRepository.findAll();
        List<LeaderboardDTO> result = new ArrayList<>();

        for (Users user : users) {
            Double avg = getAverageRating(user.getId());
            result.add(UserDashboardMapper.toLeaderboard(
                    user.getName(),
                    avg,
                    null,
                    null
            ));
        }

        result.sort(Comparator.comparing(LeaderboardDTO::getAverageRating, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        return applyPage(result, pageable);
    }

    public List<LeaderboardDTO> getTopUsersBySalary() {
        return getHighestSalaryUsers(Pageable.ofSize(10));
    }

    public List<LeaderboardDTO> getTopUsersBySalary(Pageable pageable) {
        return getHighestSalaryUsers(pageable);
    }

    public List<LeaderboardDTO> getLeaderboard() {
        return getLeaderboard(Pageable.ofSize(10));
    }

    public List<LeaderboardDTO> getLeaderboard(Pageable pageable) {
        List<Users> users = userRepository.findAll();
        List<LeaderboardDTO> result = new ArrayList<>();

        for (Users user : users) {
            Double avg = getAverageRating(user.getId());
            BigDecimal salary = null;
            try {
                salary = compensationService.getCurrentByUser(user.getId()).getSalary();
            } catch (RuntimeException ignored) {
                // no current compensation
            }
            double combined = calculateCombinedScore(avg, salary);

            result.add(UserDashboardMapper.toLeaderboard(
                    user.getName(),
                    avg,
                    salary,
                    combined
            ));
        }

        result.sort(Comparator.comparing(LeaderboardDTO::getCombinedScore, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        return applyPage(result, pageable);
    }

    public UserSummaryCardDTO getUserSummaryCard(Long userId) {
        UserDTO user = userService.getByUserID(userId);
        Double avg = getAverageRating(userId);
        Long totalReviews = (long) getReviewsForUser(userId).size();
        BigDecimal salary = null;
        try {
            salary = compensationService.getCurrentByUser(userId).getSalary();
        } catch (RuntimeException ignored) {
            // no current compensation
        }

        return UserDashboardMapper.toSummaryCard(user, avg, totalReviews, salary);
    }

    // ── Advanced ─────────────────────────────────────────────────────────────

    public UserAnalyticsDTO getUserAnalytics(Long userId) {
        UserDTO user = userService.getByUserID(userId);
        Double avg = getAverageRating(userId);
        Long totalReviews = (long) getReviewsForUser(userId).size();
        CompensationDTO current = null;
        try {
            current = compensationService.getCurrentByUser(userId);
        } catch (RuntimeException ignored) {
            // no current compensation
        }

        return UserDashboardMapper.toAnalytics(
                user,
                avg,
                totalReviews,
                current,
                getSalaryGrowth(userId)
        );
    }

    public SystemStatsDTO getSystemWideStats() {
        List<Users> users = userRepository.findAll();
        long totalUsers = users.size();
        long totalReviews = 0;
        double ratingSum = 0.0;
        long ratingCount = 0;
        BigDecimal totalPayroll = BigDecimal.ZERO;
        long usersWithNoReviews = 0;
        long usersWithNoCompensation = 0;

        for (Users user : users) {
            List<ReviewsDTO> reviews = getReviewsForUser(user.getId());
            if (reviews.isEmpty()) {
                usersWithNoReviews++;
            }
            totalReviews += reviews.size();
            for (ReviewsDTO r : reviews) {
                ratingSum += r.getRating();
                ratingCount++;
            }

            List<CompensationDTO> history = getCompensationHistory(user.getId());
            if (history.isEmpty()) {
                usersWithNoCompensation++;
            }
            try {
                CompensationDTO current = compensationService.getCurrentByUser(user.getId());
                if (current.getSalary() != null) {
                    totalPayroll = totalPayroll.add(current.getSalary());
                }
                if (current.getBonus() != null) {
                    totalPayroll = totalPayroll.add(current.getBonus());
                }
            } catch (RuntimeException ignored) {
                // no current compensation
            }
        }

        Double avg = ratingCount == 0 ? 0.0 : ratingSum / ratingCount;
        return SystemStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalReviews(totalReviews)
                .averageRating(round(avg, 2))
                .totalPayroll(totalPayroll)
                .usersWithNoReviews(usersWithNoReviews)
                .usersWithNoCompensation(usersWithNoCompensation)
                .build();
    }

    public List<UserSummaryCardDTO> getUsersWithNoReviews() {
        List<Users> users = userRepository.findAll();
        List<UserSummaryCardDTO> result = new ArrayList<>();

        for (Users user : users) {
            List<ReviewsDTO> reviews = getReviewsForUser(user.getId());
            if (reviews.isEmpty()) {
                result.add(UserDashboardMapper.toSummaryCard(userService.getByUserID(user.getId()), 0.0, 0L, null));
            }
        }
        return result;
    }

    public List<UserSummaryCardDTO> getUsersWithNoCompensation() {
        List<Users> users = userRepository.findAll();
        List<UserSummaryCardDTO> result = new ArrayList<>();

        for (Users user : users) {
            List<CompensationDTO> history = getCompensationHistory(user.getId());
            if (history.isEmpty()) {
                result.add(UserDashboardMapper.toSummaryCard(
                        userService.getByUserID(user.getId()),
                        getAverageRating(user.getId()),
                        (long) getReviewsForUser(user.getId()).size(),
                        null
                ));
            }
        }
        return result;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private CompensationSummaryDTO buildCompensationSummary(Long userId) {
        List<CompensationDTO> history = getCompensationHistory(userId);
        CompensationDTO current = null;
        try {
            current = compensationService.getCurrentByUser(userId);
        } catch (RuntimeException ignored) {
            // no current compensation
        }

        return CompensationSummaryDTO.builder()
                .current(current)
                .history(history)
                .salaryGrowth(getSalaryGrowth(userId))
                .build();
    }

    private ReviewSummaryDTO buildReviewSummary(Long userId) {
        List<ReviewsDTO> reviews = getReviewsForUser(userId);
        return ReviewSummaryDTO.builder()
                .averageRating(calculateAverageRating(reviews))
                .totalReviews((long) reviews.size())
                .ratingDistribution(calculateRatingDistribution(reviews))
                .recentReviews(getRecentReviews(userId))
                .build();
    }

    private List<ReviewsDTO> getReviewsForUser(Long userId) {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setUserId(userId);
        return reviewsService.getReviewsForUser(dto);
    }

    private Double calculateAverageRating(List<ReviewsDTO> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (ReviewsDTO r : reviews) {
            sum += r.getRating();
        }
        return round(sum / reviews.size(), 2);
    }

    private List<Long> calculateRatingDistribution(List<ReviewsDTO> reviews) {
        List<Long> distribution = new ArrayList<>(List.of(0L, 0L, 0L, 0L, 0L));
        if (reviews == null || reviews.isEmpty()) {
            return distribution;
        }

        for (ReviewsDTO r : reviews) {
            int idx = (int) Math.floor(r.getRating()) - 1;
            if (idx < 0) idx = 0;
            if (idx > 4) idx = 4;
            distribution.set(idx, distribution.get(idx) + 1);
        }
        return distribution;
    }

    private double calculateCombinedScore(Double avg, BigDecimal salary) {
        double ratingScore = avg != null ? avg : 0.0;
        double salaryScore = salary != null ? salary.doubleValue() / 1000d : 0.0;
        return round(ratingScore * 20d + salaryScore, 2);
    }

    private double round(double value, int scale) {
        return BigDecimal.valueOf(value)
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private <T> List<T> applyPage(List<T> list, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return list;
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        if (start >= list.size()) {
            return List.of();
        }
        return list.subList(start, end);
    }
}
