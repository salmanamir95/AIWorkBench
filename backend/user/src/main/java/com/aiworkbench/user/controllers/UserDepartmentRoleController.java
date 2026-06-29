package com.aiworkbench.user.controllers;

import com.aiworkbench.user.dtos.UserDepartmentRoleDTO;
import com.aiworkbench.user.services.UserDepartmentRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/udr") // Matches /user/udr after Gateway stripPrefix(1)
@RequiredArgsConstructor
public class UserDepartmentRoleController {

    private final UserDepartmentRoleService udrService;

    @GetMapping("/user/{userId}/history")
    public ResponseEntity<List<UserDepartmentRoleDTO>> getAssignmentHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(udrService.getAssignmentHistory(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<UserDepartmentRoleDTO> getActiveAssignment(@PathVariable Long userId) {
        return udrService.getActiveAssignment(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/assign")
    public ResponseEntity<UserDepartmentRoleDTO> assignUserToDepartment(@RequestBody UserDepartmentRoleDTO dto) {
        return ResponseEntity.ok(udrService.assignUserToDepartment(dto));
    }

    @PostMapping("/{id}/terminate")
    public ResponseEntity<Void> terminateAssignment(@PathVariable Long id) {
        udrService.terminateAssignment(id);
        return ResponseEntity.noContent().build();
    }
}