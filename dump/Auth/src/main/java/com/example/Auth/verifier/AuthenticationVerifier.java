package com.example.Auth.verifier;

import org.springframework.stereotype.Component;

import com.example.Auth.db.models.UserAuth;

@Component
public class AuthenticationVerifier {

    public void verify(final UserAuth user) {

        if (user.isAccountLocked()) {
            throw new IllegalStateException("Account is locked");
        }

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email is not verified");
        }

    }
}
