package com.aiworkbench.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.aiworkbench.auth.dtos.AuthProvisioningDto;
import com.aiworkbench.auth.entities.User;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "passwordHash", source = "password")
    User toEntity(AuthProvisioningDto dto);
}