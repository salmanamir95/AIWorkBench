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

import com.example.Auth.db.models.UserInfo;
import com.example.Auth.db.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserThrowsWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("Jane", "taken@example.com", "raw", 26)
        );
    }

    @Test
    void createUserEncodesPasswordAndSaves() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("raw-pass")).thenReturn("$argon2id$hash");
        when(userRepository.save(any(UserInfo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final UserInfo created = userService.createUser("Jane", "new@example.com", "raw-pass", 26);

        final ArgumentCaptor<UserInfo> captor = ArgumentCaptor.forClass(UserInfo.class);
        verify(userRepository).save(captor.capture());
        final UserInfo saved = captor.getValue();

        assertEquals("Jane", saved.getName());
        assertEquals("new@example.com", saved.getEmail());
        assertEquals(26, saved.getAge());
        assertEquals("Jane", created.getName());
        assertEquals("new@example.com", created.getEmail());
    }

    @Test
    void updateUserChangesNameAndAge() {
        final UserInfo existing = new UserInfo("Old", "user@example.com", "$argon2id$hash", 20);
        when(userRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(UserInfo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final UserInfo updated = userService.updateUser(7L, "New", 30);

        assertEquals("New", updated.getName());
        assertEquals(30, updated.getAge());
    }
}
