package com.example.Auth.Manager;

import org.springframework.stereotype.Component;

import com.example.Auth.db.models.UserAuth;
import com.example.Auth.provider.AuthenticationProvider;
import com.example.Auth.verifier.AuthenticationVerifier;

@Component
public class AuthenticationManager {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationVerifier authenticationVerifier;

    public AuthenticationManager(
            final AuthenticationProvider authenticationProvider,
            final AuthenticationVerifier authenticationVerifier
    ) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationVerifier = authenticationVerifier;
    }

    public UserAuth authenticate(
            final String email,
            final String password
    ) {

        // Step 1: Validate credentials
        final UserAuth user = authenticationProvider.authenticate(email, password);

        // Step 2: Verify account state
        authenticationVerifier.verify(user);

        return user;
    }
}
