package com.aiworkbench.user.userDashboard.dto;

import java.math.BigDecimal;
import java.util.List;

import com.aiworkbench.user.compensation.dto.CompensationDTO;

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
public class CompensationSummaryDTO {

    private CompensationDTO current;
    private List<CompensationDTO> history;
    private BigDecimal salaryGrowth;
}
