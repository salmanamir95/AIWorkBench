package com.aiworkbench.project.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aiworkbench.project.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);

    @Query("""
        SELECT DISTINCT p FROM Project p
        LEFT JOIN ProjectMember pm ON pm.project = p
        WHERE p.ownerId = :userId OR pm.userId = :userId
    """)
    Page<Project> findProjectsByUser(@Param("userId") Long userId, Pageable pageable);
}
