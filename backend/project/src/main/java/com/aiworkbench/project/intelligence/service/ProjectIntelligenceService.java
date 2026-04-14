package com.aiworkbench.project.intelligence.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aiworkbench.project.intelligence.client.ProjectServiceClient;
import com.aiworkbench.project.intelligence.client.dto.ProjectActivityLogDTO;
import com.aiworkbench.project.intelligence.client.dto.ProjectDTO;
import com.aiworkbench.project.intelligence.client.dto.ProjectMemberDTO;
import com.aiworkbench.project.intelligence.client.dto.ProjectSettingsDTO;
import com.aiworkbench.project.intelligence.dto.MemberEngagementDTO;
import com.aiworkbench.project.intelligence.dto.ProjectActivityTimelineDTO;
import com.aiworkbench.project.intelligence.dto.ProjectHealthDTO;
import com.aiworkbench.project.intelligence.dto.ProjectLeaderboardDTO;
import com.aiworkbench.project.intelligence.dto.ProjectOverviewDTO;
import com.aiworkbench.project.intelligence.dto.ProjectRiskDTO;
import com.aiworkbench.project.intelligence.mapper.ProjectIntelligenceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectIntelligenceService {

    private final ProjectServiceClient projectServiceClient;

    public ProjectOverviewDTO getProjectOverview(Long projectId) {
        ProjectDTO project = projectServiceClient.getProject(projectId);
        ProjectSettingsDTO settings = projectServiceClient.getProjectSettings(projectId);
        List<ProjectMemberDTO> members = projectServiceClient.getProjectMembers(projectId, 500);
        List<ProjectActivityLogDTO> activity = projectServiceClient.getProjectActivity(projectId, 1000);

        Long totalMembers = (long) members.size();
        Long activeMembers = getActiveMembers(activity, 30);
        LocalDateTime lastActivity = getLastActivityDate(activity);
        ProjectHealthDTO health = getProjectHealth(projectId);

        return ProjectIntelligenceMapper.toOverview(
                projectId,
                project != null ? project.getName() : null,
                project != null && project.getOwnerId() != null ? String.valueOf(project.getOwnerId()) : null,
                project != null ? project.getStatus() : null,
                settings != null ? settings.getVisibility() : null,
                totalMembers,
                activeMembers,
                lastActivity,
                health.getStatus()
        );
    }

    public ProjectHealthDTO getProjectHealth(Long projectId) {
        ProjectDTO project = projectServiceClient.getProject(projectId);
        ProjectSettingsDTO settings = projectServiceClient.getProjectSettings(projectId);
        List<ProjectMemberDTO> members = projectServiceClient.getProjectMembers(projectId, 500);
        List<ProjectActivityLogDTO> activity = projectServiceClient.getProjectActivity(projectId, 1000);

        List<String> reasons = new ArrayList<>();
        int score = 100;

        long totalMembers = members.size();
        long activeMembers = getActiveMembers(activity, 30);
        double engagementRatio = totalMembers == 0 ? 0.0 : (double) activeMembers / (double) totalMembers;

        LocalDateTime lastActivity = getLastActivityDate(activity);
        long daysSinceActivity = lastActivity == null ? 365 : ChronoUnit.DAYS.between(lastActivity, LocalDateTime.now());

        int activityFrequencyScore = (int) Math.min(30, activity.size() / 3);
        int engagementScore = (int) Math.round(engagementRatio * 30);
        int recencyScore = daysSinceActivity <= 3 ? 20 : daysSinceActivity <= 7 ? 10 : 0;

        score -= inactivityPenalty(daysSinceActivity, reasons);
        score -= roleImbalancePenalty(members, reasons);

        score = clamp(score + activityFrequencyScore + engagementScore + recencyScore, 0, 100);

        String status = score >= 80 ? "GOOD" : score >= 50 ? "WARNING" : "CRITICAL";
        if (lastActivity == null) {
            reasons.add("No activity recorded");
        }
        if (settings != null && "PRIVATE".equalsIgnoreCase(settings.getVisibility()) && daysSinceActivity > 7) {
            reasons.add("Private project with no recent activity");
        }

        return ProjectIntelligenceMapper.toHealth(projectId, score, status, reasons);
    }

    public ProjectRiskDTO getProjectRiskAnalysis(Long projectId) {
        ProjectDTO project = projectServiceClient.getProject(projectId);
        ProjectSettingsDTO settings = projectServiceClient.getProjectSettings(projectId);
        List<ProjectMemberDTO> members = projectServiceClient.getProjectMembers(projectId, 500);
        List<ProjectActivityLogDTO> activity = projectServiceClient.getProjectActivity(projectId, 1000);

        List<String> issues = new ArrayList<>();
        String riskLevel = "LOW";

        LocalDateTime lastActivity = getLastActivityDate(activity);
        long daysSinceActivity = lastActivity == null ? 365 : ChronoUnit.DAYS.between(lastActivity, LocalDateTime.now());

        if (daysSinceActivity > 7) {
            issues.add("No activity in last 7 days");
            riskLevel = "CRITICAL";
        }

        boolean ownerActive = project != null && hasUserActivity(activity, project.getOwnerId());
        if (!ownerActive) {
            issues.add("No owner activity");
            riskLevel = higherRisk(riskLevel, "HIGH");
        }

        long adminCount = members.stream()
                .filter(m -> "PROJECT_ADMIN".equalsIgnoreCase(m.getProjectRole()) || "PROJECT_OWNER".equalsIgnoreCase(m.getProjectRole()))
                .count();
        if (adminCount > Math.max(1, members.size() / 2)) {
            issues.add("Too many admins");
            riskLevel = higherRisk(riskLevel, "MEDIUM");
        }

        if (daysSinceActivity > 7 && settings != null && "PRIVATE".equalsIgnoreCase(settings.getVisibility())) {
            issues.add("No activity and private project");
            riskLevel = higherRisk(riskLevel, "WARNING");
        }

        return ProjectIntelligenceMapper.toRisk(projectId, riskLevel, issues);
    }

    public List<ProjectActivityTimelineDTO> getActivityTimeline(Long projectId) {
        List<ProjectActivityLogDTO> activity = projectServiceClient.getProjectActivity(projectId, 1000);

        Map<LocalDate, Long> counts = new HashMap<>();
        for (ProjectActivityLogDTO log : activity) {
            if (log.getCreatedAt() == null) continue;
            LocalDate date = log.getCreatedAt().toLocalDate();
            counts.put(date, counts.getOrDefault(date, 0L) + 1);
        }

        return ProjectIntelligenceMapper.toTimelineList(counts);
    }

    public MemberEngagementDTO getMemberEngagement(Long projectId) {
        List<ProjectMemberDTO> members = projectServiceClient.getProjectMembers(projectId, 500);
        List<ProjectActivityLogDTO> activity = projectServiceClient.getProjectActivity(projectId, 1000);

        long activeMembers = getActiveMembers(activity, 30);
        long inactiveMembers = Math.max(0, members.size() - activeMembers);
        String topContributor = getTopContributor(activity);
        int activityScore = calculateActivityScore(activity, members.size());

        return ProjectIntelligenceMapper.toEngagement(
                projectId,
                activeMembers,
                inactiveMembers,
                topContributor,
                activityScore
        );
    }

    public List<ProjectLeaderboardDTO> getUserContributionAcrossProjects(Long userId) {
        List<ProjectDTO> projects = projectServiceClient.getProjectsByUser(userId, 200);
        List<ProjectLeaderboardDTO> result = new ArrayList<>();

        for (ProjectDTO project : projects) {
            if (project.getId() == null) continue;
            List<ProjectActivityLogDTO> activity = projectServiceClient.getProjectActivity(project.getId(), 1000);
            int score = (int) activity.stream()
                    .filter(a -> userId != null && userId.equals(a.getUserId()))
                    .count();

            String role = projectServiceClient.getProjectMembers(project.getId(), 500).stream()
                    .filter(m -> userId != null && userId.equals(m.getUserId()))
                    .map(ProjectMemberDTO::getProjectRole)
                    .findFirst()
                    .orElse(null);

            result.add(ProjectIntelligenceMapper.toLeaderboard(
                    userId,
                    project.getId(),
                    score,
                    role
            ));
        }
        return result;
    }

    public List<ProjectLeaderboardDTO> getGlobalProjectLeaderboard() {
        List<ProjectLeaderboardDTO> leaderboard = new ArrayList<>();
        int page = 0;
        List<ProjectDTO> projects;

        do {
            projects = projectServiceClient.getAllProjects(page, 200);
            for (ProjectDTO project : projects) {
                if (project.getId() == null) continue;
                List<ProjectActivityLogDTO> activity = projectServiceClient.getProjectActivity(project.getId(), 1000);
                Map<Long, Long> userCounts = activity.stream()
                        .filter(a -> a.getUserId() != null)
                        .collect(Collectors.groupingBy(ProjectActivityLogDTO::getUserId, Collectors.counting()));

                String topRole = null;
                for (Map.Entry<Long, Long> entry : userCounts.entrySet()) {
                    Long userId = entry.getKey();
                    int score = entry.getValue().intValue();
                    String role = projectServiceClient.getProjectMembers(project.getId(), 500).stream()
                            .filter(m -> userId.equals(m.getUserId()))
                            .map(ProjectMemberDTO::getProjectRole)
                            .findFirst()
                            .orElse(null);
                    topRole = role;
                    leaderboard.add(ProjectIntelligenceMapper.toLeaderboard(
                            userId,
                            project.getId(),
                            score,
                            role
                    ));
                }

                if (topRole == null) {
                    // no members or activity
                }
            }
            page++;
        } while (!projects.isEmpty());

        leaderboard.sort(Comparator.comparing(ProjectLeaderboardDTO::getActivityScore, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        return leaderboard.stream().limit(50).collect(Collectors.toList());
    }

    public List<ProjectOverviewDTO> getInactiveProjects() {
        List<ProjectOverviewDTO> result = new ArrayList<>();
        int page = 0;
        List<ProjectDTO> projects;

        do {
            projects = projectServiceClient.getAllProjects(page, 200);
            for (ProjectDTO project : projects) {
                if (project.getId() == null) continue;
                List<ProjectActivityLogDTO> activity = projectServiceClient.getProjectActivity(project.getId(), 1000);
                LocalDateTime lastActivity = getLastActivityDate(activity);
                long days = lastActivity == null ? 365 : ChronoUnit.DAYS.between(lastActivity, LocalDateTime.now());
                if (days > 14) {
                    ProjectSettingsDTO settings = projectServiceClient.getProjectSettings(project.getId());
                    result.add(ProjectIntelligenceMapper.toOverview(
                            project.getId(),
                            project.getName(),
                            project.getOwnerId() != null ? String.valueOf(project.getOwnerId()) : null,
                            project.getStatus(),
                            settings != null ? settings.getVisibility() : null,
                            (long) projectServiceClient.getProjectMembers(project.getId(), 500).size(),
                            getActiveMembers(activity, 30),
                            lastActivity,
                            getProjectHealth(project.getId()).getStatus()
                    ));
                }
            }
            page++;
        } while (!projects.isEmpty());

        return result;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private long getActiveMembers(List<ProjectActivityLogDTO> activity, int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        Set<Long> active = activity.stream()
                .filter(a -> a.getCreatedAt() != null && a.getCreatedAt().isAfter(cutoff))
                .map(ProjectActivityLogDTO::getUserId)
                .filter(u -> u != null)
                .collect(Collectors.toSet());
        return active.size();
    }

    private LocalDateTime getLastActivityDate(List<ProjectActivityLogDTO> activity) {
        return activity.stream()
                .map(ProjectActivityLogDTO::getCreatedAt)
                .filter(d -> d != null)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    private boolean hasUserActivity(List<ProjectActivityLogDTO> activity, Long userId) {
        if (userId == null) return false;
        return activity.stream().anyMatch(a -> userId.equals(a.getUserId()));
    }

    private int inactivityPenalty(long daysSinceActivity, List<String> reasons) {
        if (daysSinceActivity > 30) {
            reasons.add("Extended inactivity");
            return 30;
        }
        if (daysSinceActivity > 14) {
            reasons.add("Inactivity over two weeks");
            return 20;
        }
        if (daysSinceActivity > 7) {
            reasons.add("Inactivity over one week");
            return 10;
        }
        return 0;
    }

    private int roleImbalancePenalty(List<ProjectMemberDTO> members, List<String> reasons) {
        if (members.isEmpty()) return 0;
        long adminCount = members.stream()
                .filter(m -> "PROJECT_ADMIN".equalsIgnoreCase(m.getProjectRole()) || "PROJECT_OWNER".equalsIgnoreCase(m.getProjectRole()))
                .count();
        double ratio = (double) adminCount / (double) members.size();
        if (ratio > 0.6) {
            reasons.add("Role imbalance: too many admins");
            return 10;
        }
        return 0;
    }

    private String higherRisk(String current, String next) {
        List<String> order = List.of("LOW", "WARNING", "MEDIUM", "HIGH", "CRITICAL");
        return order.indexOf(next) > order.indexOf(current) ? next : current;
    }

    private int calculateActivityScore(List<ProjectActivityLogDTO> activity, int memberCount) {
        if (activity.isEmpty()) return 0;
        double perMember = memberCount == 0 ? activity.size() : (double) activity.size() / (double) memberCount;
        int score = (int) Math.min(100, Math.round(perMember * 10));
        return Math.max(0, score);
    }

    private String getTopContributor(List<ProjectActivityLogDTO> activity) {
        Map<Long, Long> counts = activity.stream()
                .filter(a -> a.getUserId() != null)
                .collect(Collectors.groupingBy(ProjectActivityLogDTO::getUserId, Collectors.counting()));
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> String.valueOf(e.getKey()))
                .orElse(null);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
