package com.aiworkbench.project.settings.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aiworkbench.project.settings.entity.ProjectSettings;

public interface ProjectSettingsRepository extends JpaRepository<ProjectSettings, Long> {

    Optional<ProjectSettings> findByProjectId(Long projectId);
}
