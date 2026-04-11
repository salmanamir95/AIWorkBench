package com.aiworkbench.user.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aiworkbench.user.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

    // IMPORTANT: names are not unique → return multiple results
    Page<Users> findByName(String name, Pageable pageable);


    Optional<Users> findByAuthId(String authId);

    boolean existsByAuthId(String authId);

    @Query("SELECT u FROM Users u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Users> searchByName(@Param("name") String name, Pageable pageable);
}
