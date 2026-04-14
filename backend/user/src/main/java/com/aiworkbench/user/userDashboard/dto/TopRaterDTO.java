package com.aiworkbench.user.userDashboard.dto;

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
public class TopRaterDTO {

    private Long userId;
    private String name;
    private Long ratingsGiven;
}
