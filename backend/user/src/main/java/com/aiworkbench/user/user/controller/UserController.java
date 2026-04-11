package com.aiworkbench.user.user.controller;

import com.aiworkbench.user.gr.GR;
import com.aiworkbench.user.user.dto.UserDTO;
import com.aiworkbench.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE USER
    @PostMapping
    public ResponseEntity<GR<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);

        return ResponseEntity.ok(
                GR.success(created, "User created successfully")
        );
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<GR<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getByUserID(id);

        return ResponseEntity.ok(
                GR.success(user, "User fetched successfully")
        );
    }

    // GET ALL USERS (PAGINATED)
    @GetMapping
    public ResponseEntity<GR<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(
                GR.success(users, "Users fetched successfully")
        );
    }

    // SEARCH USERS BY NAME
    @GetMapping("/search")
    public ResponseEntity<GR<Page<UserDTO>>> searchByName(
            @RequestParam String name,
            Pageable pageable
    ) {
        Page<UserDTO> result = userService.searchByName(pageable, name);

        return ResponseEntity.ok(
                GR.success(result, "Search completed successfully")
        );
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<GR<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO
    ) {
        UserDTO updated = userService.updateUser(id, userDTO);

        return ResponseEntity.ok(
                GR.success(updated, "User updated successfully")
        );
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<GR<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.ok(
                GR.success(null, "User deleted successfully")
        );
    }
}