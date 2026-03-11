package com.example.Auth.db.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Auth.db.models.UserAuth;
import com.example.Auth.db.repository.UserAuthRepository;

@Service
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAuthService(
            final UserAuthRepository userAuthRepository,
            final PasswordEncoder passwordEncoder
    ) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- SIGNUP ----------

    @Transactional
    public UserAuth registerUser(
            final String email,
            final String rawPassword
    ) {
        if (userAuthRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        final String hashedPassword = passwordEncoder.encode(rawPassword);

        final UserAuth user = new UserAuth(
                email,
                hashedPassword
        );

        return userAuthRepository.save(user);
    }

    // ---------- READ ----------

    @Transactional(readOnly = true)
    public UserAuth getUserById(final Long id) {
        return userAuthRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserAuth getUserByEmail(final String email) {
        return userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }

    // ---------- PASSWORD CHANGE ----------

    @Transactional
    public void changePassword(
            final Long userId,
            final String rawNewPassword
    ) {
        final UserAuth user = getUserById(userId);

        final String hashedPassword = passwordEncoder.encode(rawNewPassword);

        user.changePassword(hashedPassword);

        userAuthRepository.save(user);
    }

    // ---------- ACCOUNT SECURITY ----------

    @Transactional
    public void lockAccount(final Long userId) {
        final UserAuth user = getUserById(userId);
        user.lockAccount();
        userAuthRepository.save(user);
    }

    @Transactional
    public void unlockAccount(final Long userId) {
        final UserAuth user = getUserById(userId);
        user.unlockAccount();
        userAuthRepository.save(user);
    }

    @Transactional
    public void verifyEmail(final Long userId) {
        final UserAuth user = getUserById(userId);
        user.verifyEmail();
        userAuthRepository.save(user);
    }

    // ---------- DELETE (Optional for admin) ----------

    @Transactional
    public void deleteUser(final Long userId) {
        final UserAuth user = getUserById(userId);
        userAuthRepository.delete(user);
    }

}