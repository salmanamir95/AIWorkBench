package com.aiworkbench.user.userDashboard.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiworkbench.user.gr.GR;
import com.aiworkbench.user.userDashboard.dto.LeaderboardDTO;
import com.aiworkbench.user.userDashboard.dto.SystemStatsDTO;
import com.aiworkbench.user.userDashboard.dto.UserAnalyticsDTO;
import com.aiworkbench.user.userDashboard.dto.UserDashboardDTO;
import com.aiworkbench.user.userDashboard.service.UserDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-dashboard")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserDashboardService userDashboardService;

    @GetMapping("/user/{id}")
    public ResponseEntity<GR<UserDashboardDTO>> getUser(@PathVariable Long id) {
        UserDashboardDTO dto = userDashboardService.getUserProfile(id);
        return ResponseEntity.ok(GR.success(dto, "User profile fetched successfully"));
    }

    @GetMapping("/user/{id}/full")
    public ResponseEntity<GR<UserDashboardDTO>> getFullUser(@PathVariable Long id) {
        UserDashboardDTO dto = userDashboardService.getFullUserDashboard(id);
        return ResponseEntity.ok(GR.success(dto, "User dashboard fetched successfully"));
    }

    @GetMapping("/user/{id}/reviews")
    public ResponseEntity<GR<UserDashboardDTO>> getUserReviews(@PathVariable Long id) {
        UserDashboardDTO dto = userDashboardService.getUserWithReviews(id);
        return ResponseEntity.ok(GR.success(dto, "User reviews fetched successfully"));
    }

    @GetMapping("/user/{id}/compensation")
    public ResponseEntity<GR<UserDashboardDTO>> getUserCompensation(@PathVariable Long id) {
        UserDashboardDTO dto = userDashboardService.getUserWithCompensation(id);
        return ResponseEntity.ok(GR.success(dto, "User compensation fetched successfully"));
    }

    @GetMapping("/user/{id}/analytics")
    public ResponseEntity<GR<UserAnalyticsDTO>> getUserAnalytics(@PathVariable Long id) {
        UserAnalyticsDTO dto = userDashboardService.getUserAnalytics(id);
        return ResponseEntity.ok(GR.success(dto, "User analytics fetched successfully"));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<GR<List<LeaderboardDTO>>> getLeaderboard(Pageable pageable) {
        List<LeaderboardDTO> list = userDashboardService.getLeaderboard(pageable);
        return ResponseEntity.ok(GR.success(list, "Leaderboard fetched successfully"));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<GR<List<LeaderboardDTO>>> getTopRated(Pageable pageable) {
        List<LeaderboardDTO> list = userDashboardService.getTopUsersByRating(pageable);
        return ResponseEntity.ok(GR.success(list, "Top rated users fetched successfully"));
    }

    @GetMapping("/top-paid")
    public ResponseEntity<GR<List<LeaderboardDTO>>> getTopPaid(Pageable pageable) {
        List<LeaderboardDTO> list = userDashboardService.getTopUsersBySalary(pageable);
        return ResponseEntity.ok(GR.success(list, "Top paid users fetched successfully"));
    }

    @GetMapping("/stats/system")
    public ResponseEntity<GR<SystemStatsDTO>> getSystemStats() {
        SystemStatsDTO stats = userDashboardService.getSystemWideStats();
        return ResponseEntity.ok(GR.success(stats, "System stats fetched successfully"));
    }
}
