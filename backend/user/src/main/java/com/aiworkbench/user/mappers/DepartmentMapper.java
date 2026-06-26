package com.aiworkbench.user.mappers;

import org.springframework.stereotype.Component;

import com.aiworkbench.user.dtos.DepartmentDTO;
import com.aiworkbench.user.entities.Departments;

@Component
public class DepartmentMapper {

    public DepartmentDTO toDTO(Departments dept) {
        return DepartmentDTO.builder()
                .name(dept.getName())
                .costCenterCode(dept.getCostCenterCode())
                .createdAt(dept.getCreatedAt())
                .updatedAt(dept.getUpdatedAt())
                .build();
    }

    public Departments toEntity(DepartmentDTO dto) {
        return Departments.builder()
                .name(dto.name())
                .costCenterCode(dto.costCenterCode())
                .build();
    }
}
