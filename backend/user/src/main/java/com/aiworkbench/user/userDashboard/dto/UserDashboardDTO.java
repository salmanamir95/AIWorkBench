package com.aiworkbench.user.userDashboard.dto;

import java.time.LocalDate;

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
public class UserDashboardDTO {

    private Long id;
    private String username;
    private String email;
    private String name;
    private LocalDate dob;
    private CompensationSummaryDTO compensation;
    private ReviewSummaryDTO reviews;
}
