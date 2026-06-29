package com.aiworkbench.user.dtos;

import java.time.LocalDateTime;
import lombok.Builder;

/**
 * DTO for User-Department-Role assignment.
 * Encapsulates the assignment details by referencing the IDs of the associated entities.
 */
@Builder
public record UserDepartmentRoleDTO(
    Long userId,
    Long departmentId,
    Long roleId,
    LocalDateTime assignedAt,
    LocalDateTime endedAt,
    Boolean isActive
) {}