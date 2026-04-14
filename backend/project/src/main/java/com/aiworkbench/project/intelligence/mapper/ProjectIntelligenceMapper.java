package com.aiworkbench.project.intelligence.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.aiworkbench.project.intelligence.dto.MemberEngagementDTO;
import com.aiworkbench.project.intelligence.dto.ProjectActivityTimelineDTO;
import com.aiworkbench.project.intelligence.dto.ProjectHealthDTO;
import com.aiworkbench.project.intelligence.dto.ProjectLeaderboardDTO;
import com.aiworkbench.project.intelligence.dto.ProjectOverviewDTO;
import com.aiworkbench.project.intelligence.dto.ProjectRiskDTO;

public class ProjectIntelligenceMapper {

    public static ProjectOverviewDTO toOverview(Long projectId,
                                                String name,
                                                String owner,
                                                String status,
                                                String visibility,
                                                Long totalMembers,
                                                Long activeMembers,
                                                LocalDateTime lastActivityDate,
                                                String healthStatus) {
        return ProjectOverviewDTO.builder()
                .projectId(projectId)
                .name(name)
                .owner(owner)
                .status(status)
                .visibility(visibility)
                .totalMembers(totalMembers)
                .activeMembers(activeMembers)
                .lastActivityDate(lastActivityDate)
                .healthStatus(healthStatus)
                .build();
    }

    public static ProjectHealthDTO toHealth(Long projectId,
                                            Integer healthScore,
                                            String status,
                                            List<String> reasons) {
        return ProjectHealthDTO.builder()
                .projectId(projectId)
                .healthScore(healthScore)
                .status(status)
                .reasons(reasons)
                .build();
    }

    public static ProjectRiskDTO toRisk(Long projectId,
                                        String riskLevel,
                                        List<String> issues) {
        return ProjectRiskDTO.builder()
                .projectId(projectId)
                .riskLevel(riskLevel)
                .issues(issues)
                .build();
    }

    public static MemberEngagementDTO toEngagement(Long projectId,
                                                   Long activeMembers,
                                                   Long inactiveMembers,
                                                   String topContributor,
                                                   Integer activityScore) {
        return MemberEngagementDTO.builder()
                .projectId(projectId)
                .activeMembers(activeMembers)
                .inactiveMembers(inactiveMembers)
                .topContributor(topContributor)
                .activityScore(activityScore)
                .build();
    }

    public static ProjectLeaderboardDTO toLeaderboard(Long userId,
                                                      Long projectId,
                                                      Integer activityScore,
                                                      String role) {
        return ProjectLeaderboardDTO.builder()
                .userId(userId)
                .projectId(projectId)
                .activityScore(activityScore)
                .role(role)
                .build();
    }

    public static ProjectActivityTimelineDTO toTimeline(LocalDate date, Long count) {
        return ProjectActivityTimelineDTO.builder()
                .date(date)
                .activityCount(count)
                .build();
    }

    public static List<ProjectActivityTimelineDTO> toTimelineList(Map<LocalDate, Long> counts) {
        return counts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> toTimeline(e.getKey(), e.getValue()))
                .toList();
    }
}
