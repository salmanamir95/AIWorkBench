package com.aiworkbench.user.mappers;

import org.springframework.stereotype.Component;

import com.aiworkbench.user.dtos.EmploymentHistoryDTO;
import com.aiworkbench.user.entities.EmploymentHistory;
import com.aiworkbench.user.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmploymentHistoryMapper {

    private final UserRepository userRepository;

    public EmploymentHistoryDTO toDTO(EmploymentHistory entity) {
        if (entity == null) return null;

        return EmploymentHistoryDTO.builder()
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .salary(entity.getSalary())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    public EmploymentHistory toEntity(EmploymentHistoryDTO dto) {
        if (dto == null) return null;

        EmploymentHistory.EmploymentHistoryBuilder builder = EmploymentHistory.builder()
                .salary(dto.salary())
                .startDate(dto.startDate())
                .endDate(dto.endDate());

        // Resolve the User entity using the provided userId
        if (dto.userId() != null) {
            userRepository.findById(dto.userId())
                    .ifPresent(builder::user);
        }

        return builder.build();
    }
}