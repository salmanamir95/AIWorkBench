package com.aiworkbench.user.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class userController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/user")
    public String userService() {
        return "Hey I am User Service 🚀"+ serverPort;
    }
    
}
