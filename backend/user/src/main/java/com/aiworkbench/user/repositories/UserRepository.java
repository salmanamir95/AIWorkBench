package com.aiworkbench.user.repositories;

import com.aiworkbench.user.entities.Users;
import com.aiworkbench.user.entities.enums.user_status;
import org.springframework.data.domain.Pageable; // Correct Import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    List<Users> findByFullNameContainingIgnoreCase(String fullName);
    List<Users> findByBaseSalaryBetween(BigDecimal start, BigDecimal end);
    List<Users> findByBaseSalaryGreaterThan(BigDecimal salary);
    List<Users> findByBaseSalaryLessThan(BigDecimal salary);
    List<Users> findByOrderByBaseSalaryDesc(Pageable pageable);
    List<Users> findByOrderByBaseSalaryAsc(Pageable pageable);
    List<Users> findByStatus(user_status status);
}