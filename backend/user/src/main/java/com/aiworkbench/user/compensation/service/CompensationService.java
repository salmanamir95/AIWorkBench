package com.aiworkbench.user.compensation.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aiworkbench.user.compensation.dto.CompensationDTO;
import com.aiworkbench.user.compensation.entity.Compensation;
import com.aiworkbench.user.compensation.mapper.CompensationMapper;
import com.aiworkbench.user.compensation.repository.CompensationRepository;
import com.aiworkbench.user.user.entity.Users;
import com.aiworkbench.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompensationService {

    private final CompensationRepository compensationRepository;
    private final UserRepository userRepository;

    // CREATE compensation
    public CompensationDTO create(CompensationDTO dto) {

        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Compensation comp = CompensationMapper.toEntity(dto, user);

        return CompensationMapper.toDTO(compensationRepository.save(comp));
    }

    // GET CURRENT ACTIVE COMPENSATION
    public CompensationDTO getCurrentByUser(Long userId) {

        Compensation comp = compensationRepository
                .findActiveCompensation(userId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No active compensation"));

        return CompensationMapper.toDTO(comp);
    }

    // GET ALL COMPENSATIONS OF USER
    public Page<CompensationDTO> getByUser(Long userId, Pageable pageable) {
        return compensationRepository.findByUserId(userId, pageable)
                .map(CompensationMapper::toDTO);
    }

    // UPDATE (based on user + effective date)
    public CompensationDTO update(Long userId, LocalDate effectiveFrom, CompensationDTO dto) {

        Compensation comp = compensationRepository
                .findByUserIdAndEffectiveFrom(userId, effectiveFrom)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        comp.setSalary(dto.getSalary());
        comp.setBonus(dto.getBonus());
        comp.setCurrency(dto.getCurrency());
        comp.setEffectiveTo(dto.getEffectiveTo());

        return CompensationMapper.toDTO(compensationRepository.save(comp));
    }

    // DELETE (based on user + period, not ID)
    public void delete(Long userId, LocalDate effectiveFrom) {

        Compensation comp = compensationRepository
                .findByUserIdAndEffectiveFrom(userId, effectiveFrom)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        compensationRepository.delete(comp);
    }
}
