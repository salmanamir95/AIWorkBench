package com.aiworkbench.user.user.mapper;

import com.aiworkbench.user.user.dto.UserDTO;
import com.aiworkbench.user.user.entity.Users;

public class UserMapper {
     // DTO → Entity
    public static Users toEntity(UserDTO dto) {
        if (dto == null) return null;

        Users user = new Users();
        user.setAuthId(dto.getAuthId());
        user.setName(dto.getName());
        user.setDob(dto.getDob());

        return user;
    }

    // Entity → DTO
    public static UserDTO toDTO(Users user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setAuthId(user.getAuthId());
        dto.setName(user.getName());
        dto.setDob(user.getDob());

        return dto;
    }

}
