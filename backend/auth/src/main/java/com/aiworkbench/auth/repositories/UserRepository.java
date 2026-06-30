package com.aiworkbench.auth.repositories;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.aiworkbench.auth.entities.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, UUID> {
    Mono<User> findByEmail(String email);
    Mono<User> findByUserId(Long userId);
}