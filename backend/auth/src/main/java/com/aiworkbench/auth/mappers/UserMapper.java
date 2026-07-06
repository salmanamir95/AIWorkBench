package com.aiworkbench.auth.mappers;

import com.aiworkbench.auth.entities.User;
import com.aiworkbench.auth.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Entity to DTO
    UserDTO toDTO(User user);

    // DTO to Entity (useful for registration/updates)
    User toEntity(UserDTO userDTO);
}