package com.example.Auth.db.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Auth.db.models.UserInfo;
import com.example.Auth.db.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        final UserRepository userRepository,
        final PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- CREATE ----------

    @Transactional
    public UserInfo createUser(
        final String name,
        final String email,
        final String rawPassword,
        final Integer age
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        final String hashedPassword = passwordEncoder.encode(rawPassword);
        final UserInfo user = new UserInfo(name, email, hashedPassword, age);
        return userRepository.save(user);
        // @PrePersist fires here → validates hash, email, name before INSERT
    }

    // ---------- READ ----------

    @Transactional(readOnly = true)
    public UserInfo getUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserInfo getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }

    // ---------- UPDATE ----------

    @Transactional
    public UserInfo updateUser(
        final Long id,
        final String newName,
        final Integer newAge
    ) {
        final UserInfo user = getUserById(id);
        user.changeName(newName);
        user.changeAge(newAge);
        return userRepository.save(user);
        // @PreUpdate fires here → re-validates before UPDATE
    }

    @Transactional
    public void changePassword(final Long id, final String rawNewPassword) {
        final UserInfo user = getUserById(id);
        final String hashedPassword = passwordEncoder.encode(rawNewPassword);
        user.changePassword(hashedPassword);
        userRepository.save(user);
        // @PreUpdate fires here → validates new hash before UPDATE
    }

    // ---------- DELETE ----------

    @Transactional
    public void deleteUser(final Long id) {
        final UserInfo user = getUserById(id);
        userRepository.delete(user);
    }
}