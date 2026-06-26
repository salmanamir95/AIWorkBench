package com.aiworkbench.user.services;

import com.aiworkbench.user.dtos.DepartmentDTO;
import com.aiworkbench.user.mappers.DepartmentMapper;
import com.aiworkbench.user.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public Optional<DepartmentDTO> getById(Long id) {
        return departmentRepository.findById(id).map(departmentMapper::toDTO);
    }

    public List<DepartmentDTO> getAll() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDTO)
                .toList();
    }

    public Optional<DepartmentDTO> getByName(String name) {
        return departmentRepository.findByName(name).map(departmentMapper::toDTO);
    }

    public Optional<DepartmentDTO> getByCostCenterCode(String code) {
        return departmentRepository.findByCostCenterCode(code).map(departmentMapper::toDTO);
    }

    public List<DepartmentDTO> searchDepartments(String query) {
        return departmentRepository.findByNameContainingIgnoreCase(query).stream()
                .map(departmentMapper::toDTO)
                .toList();
    }

    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        if (departmentRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        var entity = departmentRepository.save(departmentMapper.toEntity(dto));
        return departmentMapper.toDTO(entity);
    }
}