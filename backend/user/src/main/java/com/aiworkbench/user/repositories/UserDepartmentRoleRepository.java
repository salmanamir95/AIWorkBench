package com.aiworkbench.user.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aiworkbench.user.entities.UserDepartmentRole;

@Repository
public interface UserDepartmentRoleRepository extends JpaRepository<UserDepartmentRole, Long> {

    // Find the currently active assignment for a user
    @Query("SELECT udr FROM UserDepartmentRole udr WHERE udr.user.id = :userId AND udr.isActive = true")
    Optional<UserDepartmentRole> findActiveAssignmentByUserId(@Param("userId") Long userId);

    // Find all users in a specific department (active ones)
    @Query("SELECT udr FROM UserDepartmentRole udr WHERE udr.department.id = :deptId AND udr.isActive = true")
    List<UserDepartmentRole> findActiveAssignmentsByDepartmentId(@Param("deptId") Long deptId);

    // Find all history of assignments for a user (including ended ones)
    List<UserDepartmentRole> findByUserIdOrderByAssignedAtDesc(Long userId);

}
