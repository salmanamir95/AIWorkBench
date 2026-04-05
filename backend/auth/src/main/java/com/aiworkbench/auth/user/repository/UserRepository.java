package com.aiworkbench.auth.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aiworkbench.auth.user.entity.User;



public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByVerifiedTrue();
    
    List<User> findByVerifiedFalse();

    List<User> findByCreatedAtBefore(LocalDateTime time);
    List<User> findByCreatedAtAfter(LocalDateTime time);
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT u.email FROM User u")
    List<String> findAllEmails();

    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();
}
