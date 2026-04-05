package com.aiworkbench.auth.user.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aiworkbench.auth.user.entity.User;
import com.aiworkbench.auth.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already in use");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .verified(false)
                .build();

        return userRepository.save(user);
    }

    public User loginByUsername(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("invalid username"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("invalid password");
        }

        return user;
    }

    public User loginByEmail(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("invalid email"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("invalid password");
        }

        return user;
    }

    public User findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("invalid id"));

        return user;
    }

    public Page<User> getAll(Pageable pages) {
        Page<User> pgUser = userRepository.findAll(pages);
        return pgUser;
    }

    public Page<User> getAllVerifiedTrue(Pageable pageable) {
        return userRepository.findByVerifiedTrue(pageable);
    }

    public Page<User> getAllVerifiedFalse(Pageable pageable){
        return userRepository.findByVerifiedFalse(pageable);
    }

    public Page<String> getAllEmails(Pageable page){
        return userRepository.findAllEmails(page);
    }

    public Page<String> getAllUsername(Pageable page){
        return userRepository.findAllUsernames(page);
    }

    public Page<User> getAllAccountsBefore(LocalDateTime time,Pageable page){
        return userRepository.findByCreatedAtBefore(time, page);
    }

    public Page<User> getAllAccountsAfter(LocalDateTime time, Pageable page){
        return userRepository.findByCreatedAtAfter(time, page);
    }

    public Page<User> getAllAccountsInTimeRange(LocalDateTime start, LocalDateTime end, Pageable page){
        return userRepository.findByCreatedAtBetween(start, end, page);
    }

    public User update(Long id, String username, String email, String password) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (username != null && !username.isEmpty()) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already in use");
        }
        user.setUsername(username);
    }

    if (email != null && !email.isEmpty()) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }
        user.setEmail(email);
    }

    if (password != null && !password.isEmpty()) {
        user.setPassword(password);
    }

    return userRepository.save(user);
}

public void delete(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    userRepository.delete(user);
}

}
