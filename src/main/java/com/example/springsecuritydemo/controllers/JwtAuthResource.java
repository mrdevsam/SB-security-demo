package com.example.springsecuritydemo.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
@RestController
public class JwtAuthResource {
    
    @PostMapping("/authenticate")
    public Authentication authenticate(Authentication authentication) {
        return authentication;
    }
}