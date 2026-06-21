package com.aiworkbench.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class gatewayController {
    
    @GetMapping("/ping")
    public String ping() {
        return "Gateway is alive";
    }
}

