package com.example.Auth.db.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Auth.Manager.AuthenticationManager;
import com.example.Auth.db.repository.UserAuthRepository;
import com.example.Auth.provider.AuthenticationProvider;
import com.example.Auth.security.JwtService;
import com.example.Auth.verifier.AuthenticationVerifier;
import com.example.Auth.db.service.RefreshTokenService;

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
    public AuthenticationManager appAuthenticationManager(
            final AuthenticationProvider authenticationProvider,
            final AuthenticationVerifier authenticationVerifier,
            final JwtService jwtService,
            final RefreshTokenService refreshTokenService
    ) {
        return new AuthenticationManager(authenticationProvider, authenticationVerifier, jwtService, refreshTokenService);
    }

}
