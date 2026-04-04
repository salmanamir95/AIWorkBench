package com.example.Auth.db.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Auth.db.models.UserAuth;
import com.example.Auth.db.repository.UserAuthRepository;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private UserAuthRepository userAuthRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAuthService userAuthService;

    @Test
    void registerUserThrowsWhenEmailAlreadyExists() {
        when(userAuthRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> userAuthService.registerUser("taken@example.com", "raw-pass")
        );
    }

    @Test
    void registerUserEncodesPasswordAndSaves() {
        when(userAuthRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("raw-pass")).thenReturn("$argon2id$hash");
        when(userAuthRepository.save(any(UserAuth.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final UserAuth created = userAuthService.registerUser("new@example.com", "raw-pass");

        final ArgumentCaptor<UserAuth> captor = ArgumentCaptor.forClass(UserAuth.class);
        verify(userAuthRepository).save(captor.capture());
        final UserAuth saved = captor.getValue();

        assertEquals("new@example.com", saved.getEmail());
        assertEquals("$argon2id$hash", saved.getPassword());
        assertEquals("new@example.com", created.getEmail());
    }

    @Test
    void changePasswordUpdatesHashedPassword() {
        final UserAuth existing = new UserAuth("user@example.com", "$argon2id$old");
        when(userAuthRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("new-pass")).thenReturn("$argon2id$new");
        when(userAuthRepository.save(any(UserAuth.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final UserAuth updated = userAuthService.changePassword(7L, "new-pass");

        final ArgumentCaptor<UserAuth> captor = ArgumentCaptor.forClass(UserAuth.class);
        verify(userAuthRepository).save(captor.capture());
        final UserAuth saved = captor.getValue();

        assertEquals("$argon2id$new", saved.getPassword());
        assertEquals("$argon2id$new", updated.getPassword());
    }
}
