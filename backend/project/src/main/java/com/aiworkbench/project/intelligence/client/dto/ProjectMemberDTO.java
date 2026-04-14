package com.aiworkbench.project.intelligence.client.dto;

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
public class ProjectMemberDTO {

    private Long projectId;
    private Long userId;
    private String projectRole;
    private LocalDateTime joinedAt;
}
