package com.aiworkbench.user.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aiworkbench.user.entities.Users;
import com.aiworkbench.user.entities.enums.user_status;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{
    // Standard lookup by unique index
    Optional<Users> findByEmail(String email);

    // Filter by status (using the index on status column)
    List<Users> findByStatus(user_status status);

    // Useful for search functionality
    List<Users> findByFullNameContainingIgnoreCase(String fullName);

    // Business check for duplicate emails
    boolean existsByEmail(String email);

}
