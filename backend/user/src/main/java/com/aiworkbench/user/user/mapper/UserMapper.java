package com.aiworkbench.user.user.mapper;

import com.aiworkbench.user.user.dto.UserDTO;
import com.aiworkbench.user.user.entity.Users;

public class UserMapper {
     // DTO → Entity
    public static Users toEntity(UserDTO dto) {
        if (dto == null) return null;

        Users user = new Users();
        if (dto.getId() != null) {
            user.setId(dto.getId());
        }
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setDob(dto.getDob());
        if (dto.getVerified() != null) {
            user.setVerified(dto.getVerified());
        }

        return user;
    }

    // Entity → DTO
    public static UserDTO toDTO(Users user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setDob(user.getDob());
        dto.setVerified(user.getVerified());

        return dto;
    }

}
