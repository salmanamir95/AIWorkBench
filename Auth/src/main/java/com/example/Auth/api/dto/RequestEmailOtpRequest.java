package com.example.Auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestEmailOtpRequest(
        @Email
        @NotBlank
        @Size(max = 150)
        String email
) {
}
