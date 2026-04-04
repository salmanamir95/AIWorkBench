package com.example.Auth.security;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Auth.db.models.UserAuth;
import com.example.Auth.db.repository.UserAuthRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository userAuthRepository;

    public CustomUserDetailsService(final UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserAuth user = userAuthRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(
                user.getEmail(),
                user.getPassword(),
                user.isEmailVerified(),
                true,
                true,
                !user.isAccountLocked(),
                Collections.emptyList()
        );
    }
}
