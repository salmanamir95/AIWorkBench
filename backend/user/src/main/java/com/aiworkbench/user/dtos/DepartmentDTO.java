package com.aiworkbench.user.dtos;

import java.time.LocalDateTime;

public record DepartmentDTO(
    String name,
    
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
