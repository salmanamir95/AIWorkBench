package com.aiworkbench.user.compensation.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompensationDTO {

    private Long userId;

    private BigDecimal salary;

    private BigDecimal bonus;

    private String currency;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;
}