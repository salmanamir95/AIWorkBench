package com.aiworkbench.project.project.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiworkbench.project.activity.dto.ProjectActivityLogDTO;
import com.aiworkbench.project.activity.entity.ProjectActivityLog;
import com.aiworkbench.project.activity.mapper.ProjectActivityLogMapper;
import com.aiworkbench.project.activity.repository.ProjectActivityLogRepository;
import com.aiworkbench.project.member.dto.ProjectMemberDTO;
import com.aiworkbench.project.member.entity.ProjectMember;
import com.aiworkbench.project.member.entity.ProjectRole;
import com.aiworkbench.project.member.mapper.ProjectMemberMapper;
import com.aiworkbench.project.member.repository.ProjectMemberRepository;
import com.aiworkbench.project.project.dto.ProjectDTO;
import com.aiworkbench.project.project.entity.Project;
import com.aiworkbench.project.project.entity.ProjectStatus;
import com.aiworkbench.project.project.mapper.ProjectMapper;
import com.aiworkbench.project.project.repository.ProjectRepository;
import com.aiworkbench.project.settings.dto.ProjectSettingsDTO;
import com.aiworkbench.project.settings.entity.ProjectSettings;
import com.aiworkbench.project.settings.entity.ProjectVisibility;
import com.aiworkbench.project.settings.mapper.ProjectSettingsMapper;
import com.aiworkbench.project.settings.repository.ProjectSettingsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectSettingsRepository projectSettingsRepository;
    private final ProjectActivityLogRepository projectActivityLogRepository;

    @Transactional
    public ProjectDTO createProject(ProjectDTO dto, ProjectSettingsDTO settingsDTO) {
        validateProjectCreate(dto);

        Project project = ProjectMapper.toEntity(dto);
        project.setStatus(ProjectStatus.ACTIVE);
        Project saved = projectRepository.save(project);

        ProjectSettings settings = ProjectSettings.builder()
                .project(saved)
                .visibility(settingsDTO != null && settingsDTO.getVisibility() != null
                        ? settingsDTO.getVisibility()
                        : ProjectVisibility.PRIVATE)
                .allowGuestAccess(settingsDTO != null && settingsDTO.isAllowGuestAccess())
                .build();
        projectSettingsRepository.save(settings);

        ProjectMember owner = ProjectMember.builder()
                .project(saved)
                .userId(saved.getOwnerId())
                .projectRole(ProjectRole.PROJECT_OWNER)
                .build();
        projectMemberRepository.save(owner);

        logActivity(saved, saved.getOwnerId(), "PROJECT_CREATED", "Project created");
        return ProjectMapper.toDTO(saved);
    }

    @Transactional
    public ProjectDTO updateProject(Long projectId, ProjectDTO dto) {
        Project project = getProjectEntity(projectId);

        if (dto.getName() != null && !dto.getName().isBlank()) {
            project.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            project.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            project.setStatus(dto.getStatus());
        }

        Project saved = projectRepository.save(project);
        logActivity(saved, project.getOwnerId(), "PROJECT_UPDATED", "Project updated");
        return ProjectMapper.toDTO(saved);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = getProjectEntity(projectId);
        project.setStatus(ProjectStatus.DELETED);
        projectRepository.save(project);
        logActivity(project, project.getOwnerId(), "PROJECT_DELETED", "Project deleted");
    }

    public ProjectDTO getProjectById(Long projectId) {
        return ProjectMapper.toDTO(getProjectEntity(projectId));
    }

    public Page<ProjectDTO> getProjectsByUser(Long userId, Pageable pageable) {
        return projectRepository.findProjectsByUser(userId, pageable)
                .map(ProjectMapper::toDTO);
    }

    @Transactional
    public ProjectMemberDTO addMemberToProject(Long projectId, ProjectMemberDTO dto) {
        validateMemberCreate(dto);
        Project project = getProjectEntity(projectId);

        projectMemberRepository.findByProjectIdAndUserId(projectId, dto.getUserId())
                .ifPresent(m -> { throw new RuntimeException("User already a member of project"); });

        ProjectMember member = ProjectMemberMapper.toEntity(dto, project);
        ProjectMember saved = projectMemberRepository.save(member);
        logActivity(project, dto.getUserId(), "MEMBER_ADDED", "Member added to project");
        return ProjectMemberMapper.toDTO(saved);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long userId) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        projectMemberRepository.delete(member);
        logActivity(member.getProject(), userId, "MEMBER_REMOVED", "Member removed from project");
    }

    @Transactional
    public ProjectMemberDTO changeProjectRole(Long projectId, Long userId, ProjectRole role) {
        if (role == null) {
            throw new RuntimeException("Project role is required");
        }

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setProjectRole(role);
        ProjectMember saved = projectMemberRepository.save(member);
        logActivity(member.getProject(), userId, "ROLE_CHANGED", "Project role changed");
        return ProjectMemberMapper.toDTO(saved);
    }

    public Page<ProjectMemberDTO> getProjectMembers(Long projectId, Pageable pageable) {
        return projectMemberRepository.findByProjectId(projectId, pageable)
                .map(ProjectMemberMapper::toDTO);
    }

    public Page<ProjectActivityLogDTO> getProjectActivityLog(Long projectId, Pageable pageable) {
        return projectActivityLogRepository.findByProjectIdOrderByCreatedAtDesc(projectId, pageable)
                .map(ProjectActivityLogMapper::toDTO);
    }

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
        logActivity(settings.getProject(), settings.getProject().getOwnerId(), "SETTINGS_UPDATED", "Project settings updated");
        return ProjectSettingsMapper.toDTO(saved);
    }

    public List<ProjectActivityLogDTO> getProjectActivityLog(Long projectId) {
        return projectActivityLogRepository.findByProjectIdOrderByCreatedAtDesc(projectId, Pageable.unpaged())
                .map(ProjectActivityLogMapper::toDTO)
                .getContent();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Project getProjectEntity(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    private void validateProjectCreate(ProjectDTO dto) {
        if (dto == null) {
            throw new RuntimeException("Project data is required");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new RuntimeException("Project name is required");
        }
        if (dto.getOwnerId() == null) {
            throw new RuntimeException("Owner id is required");
        }
    }

    private void validateMemberCreate(ProjectMemberDTO dto) {
        if (dto == null) {
            throw new RuntimeException("Member data is required");
        }
        if (dto.getUserId() == null) {
            throw new RuntimeException("User id is required");
        }
        if (dto.getProjectRole() == null) {
            throw new RuntimeException("Project role is required");
        }
    }

    private void logActivity(Project project, Long userId, String action, String details) {
        ProjectActivityLog log = ProjectActivityLog.builder()
                .project(project)
                .userId(userId != null ? userId : project.getOwnerId())
                .action(action)
                .details(details)
                .build();
        projectActivityLogRepository.save(log);
    }
}
