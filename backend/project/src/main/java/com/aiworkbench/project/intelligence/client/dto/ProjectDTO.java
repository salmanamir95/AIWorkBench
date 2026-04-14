package com.aiworkbench.project.intelligence.client.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
