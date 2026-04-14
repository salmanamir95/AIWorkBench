package com.aiworkbench.project.member.dto;

import java.time.LocalDateTime;

import com.aiworkbench.project.member.entity.ProjectRole;

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
public class ProjectMemberDTO {

    private Long id;
    private Long projectId;
    private Long userId;
    private ProjectRole projectRole;
    private LocalDateTime joinedAt;
}
