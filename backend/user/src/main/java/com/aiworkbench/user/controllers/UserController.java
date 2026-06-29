package com.aiworkbench.user.controllers;

import com.aiworkbench.user.dtos.UserDTO;
import com.aiworkbench.user.enums.user_status;
import com.aiworkbench.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/users") // Matches /user/users after Gateway stripPrefix(1)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchByFullName(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchByFullName(query));
    }

    @GetMapping("/salary/range")
    public ResponseEntity<List<UserDTO>> getBySalaryRange(
            @RequestParam BigDecimal min, 
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(userService.getBySalaryRange(min, max));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserDTO>> getByStatus(@PathVariable user_status status) {
        return ResponseEntity.ok(userService.getByStatus(status));
    }

    @GetMapping("/top-paid")
    public ResponseEntity<List<UserDTO>> getTopPaid(@RequestParam(defaultValue = "5") int n) {
        return ResponseEntity.ok(userService.getTopPaidUsers(n));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.saveUser(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}