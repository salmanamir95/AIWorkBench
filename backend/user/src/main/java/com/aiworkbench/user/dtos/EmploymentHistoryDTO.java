package com.aiworkbench.user.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record EmploymentHistoryDTO(
    Long userId,
    BigDecimal salary,
    LocalDate startDate,
    LocalDate endDate
) {}