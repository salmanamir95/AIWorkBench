package com.aiworkbench.project.intelligence.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiworkbench.project.gr.GR;
import com.aiworkbench.project.intelligence.dto.MemberEngagementDTO;
import com.aiworkbench.project.intelligence.dto.ProjectActivityTimelineDTO;
import com.aiworkbench.project.intelligence.dto.ProjectHealthDTO;
import com.aiworkbench.project.intelligence.dto.ProjectLeaderboardDTO;
import com.aiworkbench.project.intelligence.dto.ProjectOverviewDTO;
import com.aiworkbench.project.intelligence.dto.ProjectRiskDTO;
import com.aiworkbench.project.intelligence.service.ProjectIntelligenceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/intelligence/projects")
@RequiredArgsConstructor
public class ProjectIntelligenceController {

    private final ProjectIntelligenceService projectIntelligenceService;

    @GetMapping("/overview/{projectId}")
    public ResponseEntity<GR<ProjectOverviewDTO>> getOverview(@PathVariable Long projectId) {
        return ResponseEntity.ok(GR.success(
                projectIntelligenceService.getProjectOverview(projectId),
                "Project overview fetched successfully"
        ));
    }

    @GetMapping("/health/{projectId}")
    public ResponseEntity<GR<ProjectHealthDTO>> getHealth(@PathVariable Long projectId) {
        return ResponseEntity.ok(GR.success(
                projectIntelligenceService.getProjectHealth(projectId),
                "Project health fetched successfully"
        ));
    }

    @GetMapping("/risk/{projectId}")
    public ResponseEntity<GR<ProjectRiskDTO>> getRisk(@PathVariable Long projectId) {
        return ResponseEntity.ok(GR.success(
                projectIntelligenceService.getProjectRiskAnalysis(projectId),
                "Project risk fetched successfully"
        ));
    }

    @GetMapping("/activity/{projectId}")
    public ResponseEntity<GR<List<ProjectActivityTimelineDTO>>> getActivity(@PathVariable Long projectId) {
        return ResponseEntity.ok(GR.success(
                projectIntelligenceService.getActivityTimeline(projectId),
                "Project activity timeline fetched successfully"
        ));
    }

    @GetMapping("/members/{projectId}/engagement")
    public ResponseEntity<GR<MemberEngagementDTO>> getEngagement(@PathVariable Long projectId) {
        return ResponseEntity.ok(GR.success(
                projectIntelligenceService.getMemberEngagement(projectId),
                "Member engagement fetched successfully"
        ));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<GR<List<ProjectLeaderboardDTO>>> getLeaderboard() {
        return ResponseEntity.ok(GR.success(
                projectIntelligenceService.getGlobalProjectLeaderboard(),
                "Leaderboard fetched successfully"
        ));
    }

    @GetMapping("/user/{userId}/contributions")
    public ResponseEntity<GR<List<ProjectLeaderboardDTO>>> getUserContributions(@PathVariable Long userId) {
        return ResponseEntity.ok(GR.success(
                projectIntelligenceService.getUserContributionAcrossProjects(userId),
                "User contributions fetched successfully"
        ));
    }

    @GetMapping("/inactive")
    public ResponseEntity<GR<List<ProjectOverviewDTO>>> getInactiveProjects() {
        return ResponseEntity.ok(GR.success(
                projectIntelligenceService.getInactiveProjects(),
                "Inactive projects fetched successfully"
        ));
    }
}
