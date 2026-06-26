package com.aiworkbench.user.dtos;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record DepartmentDTO(
    String name,
    String costCenterCode,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
