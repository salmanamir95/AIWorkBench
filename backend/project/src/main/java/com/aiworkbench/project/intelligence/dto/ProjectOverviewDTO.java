package com.aiworkbench.project.intelligence.dto;

import java.time.LocalDateTime;

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
public class ProjectOverviewDTO {

    private Long projectId;
    private String name;
    private String owner;
    private String status;
    private String visibility;
    private Long totalMembers;
    private Long activeMembers;
    private LocalDateTime lastActivityDate;
    private String healthStatus;
}
