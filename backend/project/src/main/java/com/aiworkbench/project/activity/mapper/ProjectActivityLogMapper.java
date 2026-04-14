package com.aiworkbench.project.activity.mapper;

import com.aiworkbench.project.activity.dto.ProjectActivityLogDTO;
import com.aiworkbench.project.activity.entity.ProjectActivityLog;
import com.aiworkbench.project.project.entity.Project;

public class ProjectActivityLogMapper {

    public static ProjectActivityLogDTO toDTO(ProjectActivityLog entity) {
        if (entity == null) return null;

        return ProjectActivityLogDTO.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .userId(entity.getUserId())
                .action(entity.getAction())
                .details(entity.getDetails())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static ProjectActivityLog toEntity(ProjectActivityLogDTO dto, Project project) {
        if (dto == null) return null;

        return ProjectActivityLog.builder()
                .id(dto.getId())
                .project(project)
                .userId(dto.getUserId())
                .action(dto.getAction())
                .details(dto.getDetails())
                .build();
    }
}
