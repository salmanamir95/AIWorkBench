package com.aiworkbench.user.compensation.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiworkbench.user.compensation.dto.CompensationDTO;
import com.aiworkbench.user.compensation.service.CompensationService;
import com.aiworkbench.user.gr.GR;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compensations")
@RequiredArgsConstructor
public class CompensationController {

    private final CompensationService compensationService;

    // CREATE compensation
    @PostMapping
    public ResponseEntity<GR<CompensationDTO>> create(@RequestBody CompensationDTO dto) {
        CompensationDTO created = compensationService.create(dto);
        return ResponseEntity.ok(GR.success(created, "Compensation created successfully"));
    }

    // GET CURRENT ACTIVE COMPENSATION
    @GetMapping("/current/{userId}")
    public ResponseEntity<GR<CompensationDTO>> getCurrentByUser(@PathVariable Long userId) {
        CompensationDTO current = compensationService.getCurrentByUser(userId);
        return ResponseEntity.ok(GR.success(current, "Current compensation fetched successfully"));
    }

    // GET ALL COMPENSATIONS OF USER (PAGINATED)
    @GetMapping("/user/{userId}")
    public ResponseEntity<GR<Page<CompensationDTO>>> getByUser(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<CompensationDTO> page = compensationService.getByUser(userId, pageable);
        return ResponseEntity.ok(GR.success(page, "Compensations fetched successfully"));
    }

    // UPDATE (based on user + effective date)
    @PutMapping("/user/{userId}")
    public ResponseEntity<GR<CompensationDTO>> update(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveFrom,
            @RequestBody CompensationDTO dto
    ) {
        CompensationDTO updated = compensationService.update(userId, effectiveFrom, dto);
        return ResponseEntity.ok(GR.success(updated, "Compensation updated successfully"));
    }

    // DELETE (based on user + period, not ID)
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<GR<Void>> delete(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveFrom
    ) {
        compensationService.delete(userId, effectiveFrom);
        return ResponseEntity.ok(GR.success(null, "Compensation deleted successfully"));
    }
}
