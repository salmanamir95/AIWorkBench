package com.aiworkbench.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aiworkbench.user.entities.Departments;

@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Long>{

    // Standard CRUD is already provided by JpaRepository

    // Find by unique business keys
    Optional<Departments> findByName(String name);
    
    Optional<Departments> findByCostCenterCode(String costCenterCode);

    // Check existence to prevent duplicate names/codes in service layer
    boolean existsByName(String name);
    boolean existsByCostCenterCode(String costCenterCode);
}
