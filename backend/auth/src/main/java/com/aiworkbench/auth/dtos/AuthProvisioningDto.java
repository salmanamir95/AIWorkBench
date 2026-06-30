package com.aiworkbench.auth.dtos;

public record AuthProvisioningDto(Long userId, String email, String password) {}