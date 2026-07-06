package com.aiworkbench.auth.dtos;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private UUID id;
    private Long userId;
    private String email;
    private Boolean enabled;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
