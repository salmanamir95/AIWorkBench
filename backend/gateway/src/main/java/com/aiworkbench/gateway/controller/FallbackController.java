package com.aiworkbench.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/user")
    public String userFallback() {
        return "User service is temporarily unavailable ⚠️";
    }
}