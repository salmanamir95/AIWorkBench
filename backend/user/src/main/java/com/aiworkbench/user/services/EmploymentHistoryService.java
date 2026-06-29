package com.aiworkbench.user.services;

import com.aiworkbench.user.dtos.EmploymentHistoryDTO;
import com.aiworkbench.user.entities.EmploymentHistory;
import com.aiworkbench.user.mappers.EmploymentHistoryMapper;
import com.aiworkbench.user.repositories.EmploymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmploymentHistoryService {

    private final EmploymentHistoryRepository repository;
    private final EmploymentHistoryMapper mapper;

    public List<EmploymentHistoryDTO> getHistoryByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public Optional<EmploymentHistoryDTO> getCurrentEmployment(Long userId) {
        return repository.findCurrentByUserId(userId)
                .map(mapper::toDTO);
    }

    @Transactional
    public EmploymentHistoryDTO createHistoryRecord(EmploymentHistoryDTO dto) {
        // Logic: Before adding new history, if there's an active record, we might need 
        // to set its end_date to today. This is a common business requirement.
        
        EmploymentHistory entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public void deleteHistoryRecord(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("History record not found with id: " + id);
        }
        repository.deleteById(id);
    }
}