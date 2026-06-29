package com.aiworkbench.user.services;

import com.aiworkbench.user.dtos.RoleDTO;
import com.aiworkbench.user.entities.Roles;
import com.aiworkbench.user.mappers.RoleMapper;
import com.aiworkbench.user.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public Optional<RoleDTO> getById(Long id) {
        return roleRepository.findById(id).map(roleMapper::toDTO);
    }

    public List<RoleDTO> getByDepartment(Long departmentId) {
        return roleRepository.findByDepartmentId(departmentId).stream()
                .map(roleMapper::toDTO)
                .toList();
    }

    @Transactional
    public RoleDTO createRole(RoleDTO dto) {
        if (roleRepository.existsByRoleName(dto.roleName())) {
            throw new IllegalArgumentException("Role name already exists: " + dto.roleName());
        }
        
        Roles entity = roleMapper.toEntity(dto);
        return roleMapper.toDTO(roleRepository.save(entity));
    }

    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }
}