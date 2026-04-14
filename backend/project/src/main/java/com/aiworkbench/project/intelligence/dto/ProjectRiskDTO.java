package com.aiworkbench.project.intelligence.dto;

import java.util.List;

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
public class ProjectRiskDTO {

    private Long projectId;
    private String riskLevel;
    private List<String> issues;
}
