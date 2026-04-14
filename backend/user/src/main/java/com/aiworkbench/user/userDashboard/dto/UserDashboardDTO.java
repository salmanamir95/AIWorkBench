package com.aiworkbench.user.userDashboard.dto;

import java.util.Date;

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

    private String authId;
    private String name;
    private Date dob;
    private CompensationSummaryDTO compensation;
    private ReviewSummaryDTO reviews;
}
