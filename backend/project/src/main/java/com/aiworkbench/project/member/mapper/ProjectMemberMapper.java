package com.aiworkbench.project.member.mapper;

import com.aiworkbench.project.member.dto.ProjectMemberDTO;
import com.aiworkbench.project.member.entity.ProjectMember;
import com.aiworkbench.project.project.entity.Project;

public class ProjectMemberMapper {

    public static ProjectMemberDTO toDTO(ProjectMember entity) {
        if (entity == null) return null;

        return ProjectMemberDTO.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .userId(entity.getUserId())
                .projectRole(entity.getProjectRole())
                .joinedAt(entity.getJoinedAt())
                .build();
    }

    public static ProjectMember toEntity(ProjectMemberDTO dto, Project project) {
        if (dto == null) return null;

        return ProjectMember.builder()
                .id(dto.getId())
                .project(project)
                .userId(dto.getUserId())
                .projectRole(dto.getProjectRole())
                .build();
    }
}
