package com.aiworkbench.user.mappers;

import com.aiworkbench.user.dtos.UserDTO;
import com.aiworkbench.user.entities.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(Users user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .baseSalary(user.getBaseSalary())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public Users toEntity(UserDTO dto) {
        return Users.builder()
                .email(dto.email()) // Note: record accessors are methods like email() not getEmail()
                .fullName(dto.fullName())
                .baseSalary(dto.baseSalary())
                .status(dto.status())
                .build();
    }
}