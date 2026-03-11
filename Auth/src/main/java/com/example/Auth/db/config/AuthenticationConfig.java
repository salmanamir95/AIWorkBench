package com.example.Auth.db.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Auth.Manager.AuthenticationManager;
import com.example.Auth.db.repository.UserAuthRepository;
import com.example.Auth.provider.AuthenticationProvider;
import com.example.Auth.verifier.AuthenticationVerifier;

@Configuration
public class AuthenticationConfig {

    @Bean
    public AuthenticationProvider authenticationProvider(
            final UserAuthRepository userAuthRepository,
            final PasswordEncoder passwordEncoder
    ) {
        return new AuthenticationProvider(userAuthRepository, passwordEncoder);
    }

    @Bean
    public AuthenticationVerifier authenticationVerifier() {
        return new AuthenticationVerifier();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationProvider authenticationProvider,
            final AuthenticationVerifier authenticationVerifier
    ) {
        return new AuthenticationManager(authenticationProvider, authenticationVerifier);
    }

}
