package com.aiworkbench.auth.user.service;

import org.springframework.stereotype.Service;

import com.aiworkbench.auth.user.entity.User;
import com.aiworkbench.auth.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(String username, String email, String password)
    {
        if (userRepository.existsByEmail(email)){
            throw new RuntimeException("Email already in use");
        }
        if (userRepository.existsByUsername(username)){
            throw new RuntimeException("Username is already in use");
        }

        User user = User.builder()
            .username(username)
            .password(password)
            .verified(false)
            .build();

        return userRepository.save(user);
    }

    public User loginByUsername(String username, String password){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("invalid username"));
        
        if (user.getPassword() != password)
            throw new RuntimeException("invalid password");

        return user;
    }

    public User loginByEmail(String email, String password){
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("invalid email"));
        
        if (user.getPassword() != password)
            throw new RuntimeException("invalid password");

        return user;
    }

}
