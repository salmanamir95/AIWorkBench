package com.aiworkbench.user.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aiworkbench.user.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

    // IMPORTANT: names are not unique → return multiple results
    Page<Users> findByName(String name, Pageable pageable);

    Page<Users> findAllByName(String name);

    Optional<Users> findByAuthId(String authId);

    boolean existsByAuthId(String authId);
}