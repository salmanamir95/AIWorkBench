package com.aiworkbench.user.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aiworkbench.user.user.dto.UserDTO;
import com.aiworkbench.user.user.entity.Users;
import com.aiworkbench.user.user.mapper.UserMapper;
import com.aiworkbench.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO createUser(UserDTO user) {
        if (user.getId() != null) {
            Users existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("Auth user not found with id: " + user.getId()));

            existingUser.setName(user.getName());
            existingUser.setDob(user.getDob());

            return UserMapper.toDTO(userRepository.save(existingUser));
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new RuntimeException("Username is required when creating a user");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email is required when creating a user");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required when creating a user");
        }

        if (user.getUsername() != null && userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        return UserMapper.toDTO(userRepository.save(UserMapper.toEntity(user)));
    }

    public UserDTO getByUserID(long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return UserMapper.toDTO(user);
    }

    public Page<UserDTO> getAllUsers(Pageable page) {
        return userRepository.findAll(page).map(UserMapper::toDTO);
    }

    public Page<UserDTO> searchByName(Pageable page, String name) {
        return userRepository.searchByName(name, page).map(UserMapper::toDTO);
    }

    public UserDTO updateUser(Long id, UserDTO updatedUser) {

        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (updatedUser.getUsername() != null
                && !updatedUser.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getEmail() != null
                && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setDob(updatedUser.getDob());
        if (updatedUser.getVerified() != null) {
            existingUser.setVerified(updatedUser.getVerified());
        }

        return UserMapper.toDTO(userRepository.save(existingUser));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

}
