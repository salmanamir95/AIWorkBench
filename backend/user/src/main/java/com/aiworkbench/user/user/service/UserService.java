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

    public Users createUser(UserDTO user) {
        if (userRepository.existsByAuthId(user.getAuthId())) {
            throw new RuntimeException("AuthID already exists");
        }

        return userRepository.save(UserMapper.toEntity(user));
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

        // Update only allowed fields
        existingUser.setName(updatedUser.getName());

        // Optional: handle authId carefully (usually shouldn't change)
        if (updatedUser.getAuthId() != null
                && !updatedUser.getAuthId().equals(existingUser.getAuthId())) {

            if (userRepository.existsByAuthId(updatedUser.getAuthId())) {
                throw new RuntimeException("AuthID already exists");
            }

            existingUser.setAuthId(updatedUser.getAuthId());
        }
        existingUser.setDob(updatedUser.getDob());

        return UserMapper.toDTO(userRepository.save(existingUser));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

}
