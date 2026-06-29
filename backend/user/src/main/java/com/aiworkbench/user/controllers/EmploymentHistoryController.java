package com.aiworkbench.user.controllers;

import com.aiworkbench.user.dtos.EmploymentHistoryDTO;
import com.aiworkbench.user.services.EmploymentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eh") // Maps to /user/eh/ after Gateway stripPrefix(1)
@RequiredArgsConstructor
public class EmploymentHistoryController {

    private final EmploymentHistoryService ehService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EmploymentHistoryDTO>> getHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ehService.getHistoryByUserId(userId));
    }

    @GetMapping("/user/{userId}/current")
    public ResponseEntity<EmploymentHistoryDTO> getCurrentEmployment(@PathVariable Long userId) {
        return ehService.getCurrentEmployment(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmploymentHistoryDTO> createHistoryRecord(@RequestBody EmploymentHistoryDTO dto) {
        return ResponseEntity.ok(ehService.createHistoryRecord(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoryRecord(@PathVariable Long id) {
        try {
            ehService.deleteHistoryRecord(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}