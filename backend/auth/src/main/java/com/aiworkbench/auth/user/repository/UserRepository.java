package com.aiworkbench.auth.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aiworkbench.auth.user.entity.User;



public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Page<User> findByVerifiedTrue(Pageable pageable);
    
    Page<User> findByVerifiedFalse(Pageable pageable);

    Page<User> findByCreatedAtBefore(LocalDateTime time, Pageable pageable);
    Page<User> findByCreatedAtAfter(LocalDateTime time, Pageable pageable);
    Page<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);


    @Query("SELECT u.email FROM User u")
    Page<String> findAllEmails(Pageable pageable);

    @Query("SELECT u.username FROM User u")
    Page<String> findAllUsernames(Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
