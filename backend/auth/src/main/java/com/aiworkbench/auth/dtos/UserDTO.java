package com.aiworkbench.auth.dtos; // Ensure this package matches your DTO path

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor; // <--- ADD THIS
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor // <--- Required for MapStruct
@AllArgsConstructor // <--- Required for Builder
public class UserDTO {
    private UUID id;
    private Long userId;
    private String email;
    private Boolean enabled;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}