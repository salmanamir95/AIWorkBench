package com.aiworkbench.user.compensation.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompensationDTO {

    private Long id;

    private Long userId;

    private BigDecimal salary;

    private BigDecimal bonus;

    private String currency;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;
}