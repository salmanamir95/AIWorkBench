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

    public Users getByUserID(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Page<Users> getAllUsers(Pageable page) {
        return userRepository.findAll(page);
    }

    public Page<Users> searchByName(Pageable page, String name) {
        return userRepository.searchByName(name, page);
    }

    public Users updateUser(Long id, UserDTO updatedUser) {

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

        return userRepository.save(existingUser);
    }

}
