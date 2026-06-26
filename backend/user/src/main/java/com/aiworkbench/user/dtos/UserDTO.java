package com.aiworkbench.user.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aiworkbench.user.enums.user_status;

import lombok.Builder;

@Builder
public record UserDTO(
    String email,
    String fullName,
    BigDecimal baseSalary,
    user_status status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
