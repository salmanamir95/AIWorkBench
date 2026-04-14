package com.aiworkbench.project.intelligence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectLeaderboardDTO {

    private Long userId;
    private Long projectId;
    private Integer activityScore;
    private String role;
}
