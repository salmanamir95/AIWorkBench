package com.aiworkbench.user.dtos;

import lombok.Builder;

@Builder
// DTO: Uses departmentId instead of the whole entity
public record RoleDTO(String roleName, Long departmentId) {}
