package com.aiworkbench.project.intelligence.client.dto;

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
public class ProjectSettingsDTO {

    private String visibility;
    private boolean allowGuestAccess;
}
