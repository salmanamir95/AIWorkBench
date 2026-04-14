package com.aiworkbench.project.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiworkbench.project.activity.service.ProjectActivityLogService;
import com.aiworkbench.project.member.dto.ProjectMemberDTO;
import com.aiworkbench.project.member.entity.ProjectMember;
import com.aiworkbench.project.member.entity.ProjectRole;
import com.aiworkbench.project.member.mapper.ProjectMemberMapper;
import com.aiworkbench.project.member.repository.ProjectMemberRepository;
import com.aiworkbench.project.project.entity.Project;
import com.aiworkbench.project.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectActivityLogService projectActivityLogService;

    @Transactional
    public ProjectMemberDTO addMemberToProject(Long projectId, ProjectMemberDTO dto) {
        validateMemberCreate(dto);
        Project project = getProjectEntity(projectId);

        projectMemberRepository.findByProjectIdAndUserId(projectId, dto.getUserId())
                .ifPresent(m -> { throw new RuntimeException("User already a member of project"); });

        ProjectMember member = ProjectMemberMapper.toEntity(dto, project);
        ProjectMember saved = projectMemberRepository.save(member);
        projectActivityLogService.logActivity(project, dto.getUserId(), "MEMBER_ADDED", "Member added to project");
        return ProjectMemberMapper.toDTO(saved);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long userId) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        projectMemberRepository.delete(member);
        projectActivityLogService.logActivity(member.getProject(), userId, "MEMBER_REMOVED", "Member removed from project");
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
        projectActivityLogService.logActivity(member.getProject(), userId, "ROLE_CHANGED", "Project role changed");
        return ProjectMemberMapper.toDTO(saved);
    }

    public Page<ProjectMemberDTO> getProjectMembers(Long projectId, Pageable pageable) {
        return projectMemberRepository.findByProjectId(projectId, pageable)
                .map(ProjectMemberMapper::toDTO);
    }

    private Project getProjectEntity(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
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
}
