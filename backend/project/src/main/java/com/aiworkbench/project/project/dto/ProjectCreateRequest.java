package com.aiworkbench.project.project.dto;

import com.aiworkbench.project.settings.dto.ProjectSettingsDTO;

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
public class ProjectCreateRequest {

    private ProjectDTO project;
    private ProjectSettingsDTO settings;
}
