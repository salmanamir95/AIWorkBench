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
public class MemberEngagementDTO {

    private Long projectId;
    private Long activeMembers;
    private Long inactiveMembers;
    private String topContributor;
    private Integer activityScore;
}
