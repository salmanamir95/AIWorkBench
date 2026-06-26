package com.aiworkbench.user.repositories;

import com.aiworkbench.user.entities.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Long> {
    Optional<Departments> findByName(String name);
    Optional<Departments> findByCostCenterCode(String costCenterCode);
    
    // Search functionality using partial name matching
    List<Departments> findByNameContainingIgnoreCase(String name);
    
    boolean existsByName(String name);
    boolean existsByCostCenterCode(String costCenterCode);
}