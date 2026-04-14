package com.aiworkbench.project.settings.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiworkbench.project.activity.service.ProjectActivityLogService;
import com.aiworkbench.project.settings.dto.ProjectSettingsDTO;
import com.aiworkbench.project.settings.entity.ProjectSettings;
import com.aiworkbench.project.settings.mapper.ProjectSettingsMapper;
import com.aiworkbench.project.settings.repository.ProjectSettingsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectSettingsService {

    private final ProjectSettingsRepository projectSettingsRepository;
    private final ProjectActivityLogService projectActivityLogService;

    public ProjectSettingsDTO getProjectSettings(Long projectId) {
        ProjectSettings settings = projectSettingsRepository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Project settings not found"));
        return ProjectSettingsMapper.toDTO(settings);
    }

    @Transactional
    public ProjectSettingsDTO updateProjectSettings(Long projectId, ProjectSettingsDTO dto) {
        ProjectSettings settings = projectSettingsRepository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Project settings not found"));

        if (dto.getVisibility() != null) {
            settings.setVisibility(dto.getVisibility());
        }
        settings.setAllowGuestAccess(dto.isAllowGuestAccess());

        ProjectSettings saved = projectSettingsRepository.save(settings);
        projectActivityLogService.logActivity(settings.getProject(), settings.getProject().getOwnerId(), "SETTINGS_UPDATED", "Project settings updated");
        return ProjectSettingsMapper.toDTO(saved);
    }
}
