package com.aiworkbench.user.compensation.mapper;

import com.aiworkbench.user.compensation.dto.CompensationDTO;
import com.aiworkbench.user.compensation.entity.Compensation;
import com.aiworkbench.user.user.entity.Users;

public class CompensationMapper {

    public static CompensationDTO toDTO(Compensation entity) {
        if (entity == null) return null;

        CompensationDTO dto = new CompensationDTO();
        dto.setUserId(entity.getUser().getId());
        dto.setSalary(entity.getSalary());
        dto.setBonus(entity.getBonus());
        dto.setCurrency(entity.getCurrency());
        dto.setEffectiveFrom(entity.getEffectiveFrom());
        dto.setEffectiveTo(entity.getEffectiveTo());

        return dto;
    }

    public static Compensation toEntity(CompensationDTO dto, Users user) {
        if (dto == null) return null;

        Compensation entity = new Compensation();
        entity.setUser(user);
        entity.setSalary(dto.getSalary());
        entity.setBonus(dto.getBonus());
        entity.setCurrency(dto.getCurrency());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());

        return entity;
    }
}