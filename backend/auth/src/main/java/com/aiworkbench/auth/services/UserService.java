package com.aiworkbench.auth.services;

import com.aiworkbench.auth.dtos.UserDTO;
import com.aiworkbench.auth.entities.User;
import com.aiworkbench.auth.mappers.UserMapper;
import com.aiworkbench.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<UserDTO> getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        // Here you would also handle password encoding
        // before saving to the database
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
}