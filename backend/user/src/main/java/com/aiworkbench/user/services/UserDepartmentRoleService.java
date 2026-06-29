package com.aiworkbench.user.services;

import com.aiworkbench.user.dtos.UserDepartmentRoleDTO;
import com.aiworkbench.user.entities.UserDepartmentRole;
import com.aiworkbench.user.mappers.UserDepartmentRoleMapper;
import com.aiworkbench.user.repositories.UserDepartmentRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDepartmentRoleService {

    private final UserDepartmentRoleRepository udrRepository;
    private final UserDepartmentRoleMapper udrMapper;

    public List<UserDepartmentRoleDTO> getAssignmentHistory(Long userId) {
        return udrRepository.findByUserIdOrderByAssignedAtDesc(userId).stream()
                .map(udrMapper::toDTO)
                .toList();
    }

    public Optional<UserDepartmentRoleDTO> getActiveAssignment(Long userId) {
        return udrRepository.findActiveAssignmentByUserId(userId)
                .map(udrMapper::toDTO);
    }

    @Transactional
    public UserDepartmentRoleDTO assignUserToDepartment(UserDepartmentRoleDTO dto) {
        // 1. Deactivate existing active assignment for this user
        udrRepository.findActiveAssignmentByUserId(dto.userId())
                .ifPresent(existing -> {
                    existing.setIsActive(false);
                    existing.setEndedAt(LocalDateTime.now());
                    udrRepository.save(existing);
                });

        // 2. Create the new assignment
        UserDepartmentRole newAssignment = udrMapper.toEntity(dto);
        newAssignment.setIsActive(true);
        newAssignment.setAssignedAt(LocalDateTime.now());

        return udrMapper.toDTO(udrRepository.save(newAssignment));
    }

    @Transactional
    public void terminateAssignment(Long id) {
        udrRepository.findById(id).ifPresent(assignment -> {
            assignment.setIsActive(false);
            assignment.setEndedAt(LocalDateTime.now());
            udrRepository.save(assignment);
        });
    }
}