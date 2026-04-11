package com.aiworkbench.user.compensation.mapper;

import com.aiworkbench.user.compensation.dto.CompensationDTO;
import com.aiworkbench.user.compensation.entity.Compensation;
import com.aiworkbench.user.user.entity.Users;

public class CompensationMapper {

    // =========================================================
    // ENTITY -> DTO
    // =========================================================
    public static CompensationDTO toDTO(Compensation comp) {
        if (comp == null) return null;

        return CompensationDTO.builder()
                .id(comp.getId())
                .userId(comp.getUser() != null ? comp.getUser().getId() : null)
                .salary(comp.getSalary())
                .bonus(comp.getBonus())
                .currency(comp.getCurrency())
                .effectiveFrom(comp.getEffectiveFrom())
                .effectiveTo(comp.getEffectiveTo())
                .build();
    }

    // =========================================================
    // DTO -> ENTITY
    // NOTE: User must be set from DB in service layer
    // =========================================================
    public static Compensation toEntity(CompensationDTO dto, Users user) {
        if (dto == null) return null;

        return Compensation.builder()
                .id(dto.getId())
                .user(user)
                .salary(dto.getSalary())
                .bonus(dto.getBonus())
                .currency(dto.getCurrency())
                .effectiveFrom(dto.getEffectiveFrom())
                .effectiveTo(dto.getEffectiveTo())
                .build();
    }
}