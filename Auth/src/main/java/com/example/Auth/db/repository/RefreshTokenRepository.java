package com.example.Auth.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Auth.db.models.RefreshToken;
import com.example.Auth.db.models.UserAuth;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

    long deleteByUserAuth(UserAuth userAuth);
}
