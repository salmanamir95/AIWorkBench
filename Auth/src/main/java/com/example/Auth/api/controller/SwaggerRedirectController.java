package com.example.Auth.api.controller;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerRedirectController implements ErrorController {

    @RequestMapping("/")
    public String redirectRootToSwagger() {
        return "redirect:/swagger-ui.html";
    }

    @RequestMapping("/error")
    public String redirectErrorToSwagger() {
        return "redirect:/swagger-ui.html";
    }
}
