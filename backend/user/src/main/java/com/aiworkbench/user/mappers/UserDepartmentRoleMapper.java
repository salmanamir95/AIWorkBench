package com.aiworkbench.user.mappers;

import com.aiworkbench.user.dtos.UserDepartmentRoleDTO;
import com.aiworkbench.user.entities.UserDepartmentRole;
import com.aiworkbench.user.repositories.DepartmentRepository;
import com.aiworkbench.user.repositories.RoleRepository;
import com.aiworkbench.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDepartmentRoleMapper {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;

    public UserDepartmentRoleDTO toDTO(UserDepartmentRole entity) {
        if (entity == null) return null;

        return UserDepartmentRoleDTO.builder()
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .departmentId(entity.getDepartment() != null ? entity.getDepartment().getId() : null)
                .roleId(entity.getRole() != null ? entity.getRole().getId() : null)
                .assignedAt(entity.getAssignedAt())
                .endedAt(entity.getEndedAt())
                .isActive(entity.getIsActive())
                .build();
    }

    public UserDepartmentRole toEntity(UserDepartmentRoleDTO dto) {
        if (dto == null) return null;

        UserDepartmentRole.UserDepartmentRoleBuilder builder = UserDepartmentRole.builder()
                .assignedAt(dto.assignedAt())
                .endedAt(dto.endedAt())
                .isActive(dto.isActive() != null ? dto.isActive() : true);

        // Resolve dependencies
        if (dto.userId() != null) {
            userRepository.findById(dto.userId()).ifPresent(builder::user);
        }
        if (dto.departmentId() != null) {
            departmentRepository.findById(dto.departmentId()).ifPresent(builder::department);
        }
        if (dto.roleId() != null) {
            roleRepository.findById(dto.roleId()).ifPresent(builder::role);
        }

        return builder.build();
    }
}