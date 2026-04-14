package com.aiworkbench.project.settings.dto;

import com.aiworkbench.project.settings.entity.ProjectVisibility;

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
public class ProjectSettingsDTO {

    private Long id;
    private Long projectId;
    private ProjectVisibility visibility;
    private boolean allowGuestAccess;
}
