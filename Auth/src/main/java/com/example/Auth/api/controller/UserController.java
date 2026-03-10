package com.example.Auth.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Auth.api.dto.ChangePasswordRequest;
import com.example.Auth.api.dto.CreateUserRequest;
import com.example.Auth.api.dto.UpdateUserRequest;
import com.example.Auth.api.dto.UserResponse;
import com.example.Auth.api.response.GenericResponse;
import com.example.Auth.db.models.UserInfo;
import com.example.Auth.db.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create user")
    public ResponseEntity<GenericResponse<UserResponse>> createUser(@Valid @RequestBody final CreateUserRequest request) {
        final UserInfo user = userService.createUser(
                request.name(),
                request.email(),
                request.password(),
                request.age()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericResponse.success(UserResponse.from(user), "User created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<GenericResponse<UserResponse>> getById(@PathVariable final Long id) {
        return ResponseEntity.ok(
                GenericResponse.success(
                        UserResponse.from(userService.getUserById(id)),
                        "User fetched successfully"
                )
        );
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get user by email")
    public ResponseEntity<GenericResponse<UserResponse>> getByEmail(@RequestParam final String email) {
        return ResponseEntity.ok(
                GenericResponse.success(
                        UserResponse.from(userService.getUserByEmail(email)),
                        "User fetched successfully"
                )
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user name and age")
    public ResponseEntity<GenericResponse<UserResponse>> updateUser(
            @PathVariable final Long id,
            @Valid @RequestBody final UpdateUserRequest request
    ) {
        final UserInfo user = userService.updateUser(id, request.name(), request.age());
        return ResponseEntity.ok(
                GenericResponse.success(UserResponse.from(user), "User updated successfully")
        );
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Change user password")
    public ResponseEntity<GenericResponse<Void>> changePassword(
            @PathVariable final Long id,
            @Valid @RequestBody final ChangePasswordRequest request
    ) {
        userService.changePassword(id, request.newPassword());
        return ResponseEntity.ok(GenericResponse.success("Password changed successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<GenericResponse<Void>> deleteUser(@PathVariable final Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(GenericResponse.success("User deleted successfully"));
    }
}
