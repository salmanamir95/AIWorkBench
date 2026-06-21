package com.aiworkbench.user.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {

    @GetMapping("/user")
    public String userService() {
        return "Hey I am User Service 🚀";
    }
}
