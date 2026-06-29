package com.aiworkbench.user.mappers;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final DepartmentRepository departmentRepository;

    public RoleDTO toDTO(Roles role) {
        if (role == null) return null;

        return RoleDTO.builder()
                .roleName(role.getRoleName())
                .departmentId(role.getDepartment() != null ? role.getDepartment().getId() : null)
                .build();
    }

    public Roles toEntity(RoleDTO dto) {
        if (dto == null) return null;

        Roles.RolesBuilder builder = Roles.builder()
                .roleName(dto.roleName());

        // Map the department if the ID is provided
        if (dto.departmentId() != null) {
            departmentRepository.findById(dto.departmentId())
                .ifPresent(builder::department);
        }

        return builder.build();
    }
}