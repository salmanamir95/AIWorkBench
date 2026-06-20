package com.aiworkbench.auth.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Connection successful! Gateway is talking to Auth Service.";
    }
}