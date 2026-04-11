package com.aiworkbench.user.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiworkbench.user.gr.GR;
import com.aiworkbench.user.user.dto.UserDTO;
import com.aiworkbench.user.user.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<GR<UserDTO>> createUser(@RequestBody UserDTO userDTO){
        try{
            return ResponseEntity.ok(
                GR.success(userService.createUser(userDTO), "User created Successfully")
            );
        }catch (RuntimeException ex){
            return ResponseEntity.ok(
                GR.error(ex.getMessage())
            );
        }
    }
}
