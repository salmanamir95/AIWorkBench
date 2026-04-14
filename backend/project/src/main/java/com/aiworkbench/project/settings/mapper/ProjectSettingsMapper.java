package com.aiworkbench.project.settings.mapper;

import com.aiworkbench.project.project.entity.Project;
import com.aiworkbench.project.settings.dto.ProjectSettingsDTO;
import com.aiworkbench.project.settings.entity.ProjectSettings;

public class ProjectSettingsMapper {

    public static ProjectSettingsDTO toDTO(ProjectSettings entity) {
        if (entity == null) return null;

        return ProjectSettingsDTO.builder()
                .visibility(entity.getVisibility())
                .allowGuestAccess(entity.isAllowGuestAccess())
                .build();
    }

    public static ProjectSettings toEntity(ProjectSettingsDTO dto, Project project) {
        if (dto == null) return null;

        return ProjectSettings.builder()
                .project(project)
                .visibility(dto.getVisibility())
                .allowGuestAccess(dto.isAllowGuestAccess())
                .build();
    }
}
