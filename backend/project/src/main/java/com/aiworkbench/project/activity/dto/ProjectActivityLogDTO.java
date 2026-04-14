package com.aiworkbench.project.activity.dto;

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
public class ProjectActivityLogDTO {

    private Long id;
    private Long projectId;
    private Long userId;
    private String action;
    private String details;
    private LocalDateTime createdAt;
}
