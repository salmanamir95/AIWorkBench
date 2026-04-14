package com.aiworkbench.project.project.mapper;

import com.aiworkbench.project.project.dto.ProjectDTO;
import com.aiworkbench.project.project.entity.Project;

public class ProjectMapper {

    public static ProjectDTO toDTO(Project entity) {
        if (entity == null) return null;

        return ProjectDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .ownerId(entity.getOwnerId())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static Project toEntity(ProjectDTO dto) {
        if (dto == null) return null;

        return Project.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .ownerId(dto.getOwnerId())
                .status(dto.getStatus())
                .build();
    }
}
