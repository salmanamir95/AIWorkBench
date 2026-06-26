package com.aiworkbench.user.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aiworkbench.user.entities.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long>  {

    List<Roles> findByDepartmentId(Long departmentId);

    // Find a role by its specific name
    Optional<Roles> findByRoleName(String roleName);

    // Check if a role name exists to prevent duplicates
    boolean existsByRoleName(String roleName);
}
