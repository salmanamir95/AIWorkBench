package com.example.Auth.provider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.Auth.db.models.UserAuth;
import com.example.Auth.db.repository.UserAuthRepository;

@Component
public class AuthenticationProvider {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationProvider(
            final UserAuthRepository userAuthRepository,
            final PasswordEncoder passwordEncoder
    ) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAuth authenticate(
            final String email,
            final String rawPassword
    ) {

        final UserAuth user = userAuthRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }
}
