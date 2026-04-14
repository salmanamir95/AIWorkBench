package com.aiworkbench.project.project.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiworkbench.project.activity.dto.ProjectActivityLogDTO;
import com.aiworkbench.project.gr.GR;
import com.aiworkbench.project.member.dto.ProjectMemberDTO;
import com.aiworkbench.project.member.entity.ProjectRole;
import com.aiworkbench.project.project.dto.ProjectCreateRequest;
import com.aiworkbench.project.project.dto.ProjectDTO;
import com.aiworkbench.project.project.service.ProjectService;
import com.aiworkbench.project.settings.dto.ProjectSettingsDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<GR<ProjectDTO>> createProject(
            @RequestBody ProjectCreateRequest request
    ) {
        ProjectDTO created = projectService.createProject(
                request != null ? request.getProject() : null,
                request != null ? request.getSettings() : null
        );
        return ResponseEntity.ok(GR.success(created, "Project created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GR<ProjectDTO>> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDTO project
    ) {
        ProjectDTO updated = projectService.updateProject(id, project);
        return ResponseEntity.ok(GR.success(updated, "Project updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GR<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(GR.success(null, "Project deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GR<ProjectDTO>> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(GR.success(project, "Project fetched successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GR<Page<ProjectDTO>>> getProjectsByUser(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<ProjectDTO> projects = projectService.getProjectsByUser(userId, pageable);
        return ResponseEntity.ok(GR.success(projects, "Projects fetched successfully"));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<GR<ProjectMemberDTO>> addMember(
            @PathVariable Long id,
            @RequestBody ProjectMemberDTO member
    ) {
        ProjectMemberDTO added = projectService.addMemberToProject(id, member);
        return ResponseEntity.ok(GR.success(added, "Member added successfully"));
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<GR<Void>> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        projectService.removeMemberFromProject(id, userId);
        return ResponseEntity.ok(GR.success(null, "Member removed successfully"));
    }

    @PutMapping("/{id}/members/{userId}/role")
    public ResponseEntity<GR<ProjectMemberDTO>> changeMemberRole(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestParam ProjectRole role
    ) {
        ProjectMemberDTO updated = projectService.changeProjectRole(id, userId, role);
        return ResponseEntity.ok(GR.success(updated, "Project role updated successfully"));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<GR<Page<ProjectMemberDTO>>> getMembers(
            @PathVariable Long id,
            Pageable pageable
    ) {
        Page<ProjectMemberDTO> members = projectService.getProjectMembers(id, pageable);
        return ResponseEntity.ok(GR.success(members, "Project members fetched successfully"));
    }

    @GetMapping("/{id}/activity")
    public ResponseEntity<GR<Page<ProjectActivityLogDTO>>> getActivity(
            @PathVariable Long id,
            Pageable pageable
    ) {
        Page<ProjectActivityLogDTO> logs = projectService.getProjectActivityLog(id, pageable);
        return ResponseEntity.ok(GR.success(logs, "Project activity fetched successfully"));
    }

    @GetMapping("/{id}/settings")
    public ResponseEntity<GR<ProjectSettingsDTO>> getSettings(@PathVariable Long id) {
        ProjectSettingsDTO settings = projectService.getProjectSettings(id);
        return ResponseEntity.ok(GR.success(settings, "Project settings fetched successfully"));
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<GR<ProjectSettingsDTO>> updateSettings(
            @PathVariable Long id,
            @RequestBody ProjectSettingsDTO settings
    ) {
        ProjectSettingsDTO updated = projectService.updateProjectSettings(id, settings);
        return ResponseEntity.ok(GR.success(updated, "Project settings updated successfully"));
    }
}
