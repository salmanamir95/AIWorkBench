package com.aiworkbench.project.activity.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiworkbench.project.activity.dto.ProjectActivityLogDTO;
import com.aiworkbench.project.activity.entity.ProjectActivityLog;
import com.aiworkbench.project.activity.mapper.ProjectActivityLogMapper;
import com.aiworkbench.project.activity.repository.ProjectActivityLogRepository;
import com.aiworkbench.project.project.entity.Project;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectActivityLogService {

    private final ProjectActivityLogRepository projectActivityLogRepository;

    public Page<ProjectActivityLogDTO> getProjectActivityLog(Long projectId, Pageable pageable) {
        return projectActivityLogRepository.findByProjectIdOrderByCreatedAtDesc(projectId, pageable)
                .map(ProjectActivityLogMapper::toDTO);
    }

    public List<ProjectActivityLogDTO> getProjectActivityLog(Long projectId) {
        return projectActivityLogRepository.findByProjectIdOrderByCreatedAtDesc(projectId, Pageable.unpaged())
                .map(ProjectActivityLogMapper::toDTO)
                .getContent();
    }

    @Transactional
    public void logActivity(Project project, Long userId, String action, String details) {
        ProjectActivityLog log = ProjectActivityLog.builder()
                .project(project)
                .userId(userId != null ? userId : project.getOwnerId())
                .action(action)
                .details(details)
                .build();
        projectActivityLogRepository.save(log);
    }
}
