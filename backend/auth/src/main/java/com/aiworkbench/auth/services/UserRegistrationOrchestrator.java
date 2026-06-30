package com.aiworkbench.auth.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.aiworkbench.auth.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationOrchestrator {
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;

    @Transactional
    public void registerUser(UserDto dto) {
        // 1. Create local record
        UserEntity user = userRepository.save(UserEntity.builder().email(dto.getEmail()).status("PENDING").build());

        try {
            // 2. Call Auth Service (Synchronous Orchestration)
            webClientBuilder.build().post()
                .uri("http://auth-service/api/auth/provision/{id}", user.getId())
                .bodyValue(new AuthProvisioningDto(user.getId(), dto.getEmail(), dto.getPassword()))
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // Wait for confirmation

            // 3. Confirm
            user.setStatus("ACTIVE");
            userRepository.save(user);
        } catch (Exception e) {
            // 4. Compensation
            userRepository.delete(user); 
            throw new RuntimeException("Registration failed, rolling back.");
        }
    }
}