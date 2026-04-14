package com.aiworkbench.project.activity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aiworkbench.project.activity.entity.ProjectActivityLog;

public interface ProjectActivityLogRepository extends JpaRepository<ProjectActivityLog, Long> {

    Page<ProjectActivityLog> findByProjectIdOrderByCreatedAtDesc(Long projectId, Pageable pageable);
}
