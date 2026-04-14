package com.aiworkbench.project.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aiworkbench.project.member.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    List<ProjectMember> findByProjectId(Long projectId);

    Page<ProjectMember> findByProjectId(Long projectId, Pageable pageable);

    List<ProjectMember> findByUserId(Long userId);
}
