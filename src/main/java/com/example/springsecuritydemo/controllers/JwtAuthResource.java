package com.example.springsecuritydemo.controllers;

import com.example.springsecuritydemo.model.JwtResponse;
import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;

@RestController
public class JwtAuthResource {
    
    private final JwtEncoder jwtEncoder;
    
    public JwtAuthResource(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    
    @PostMapping("/authenticate")
    public JwtResponse authenticate(Authentication authentication) {
        return new JwtResponse(createToken(authentication));
    }
    
    private String createToken(Authentication authentication) {
        var claims = JwtClaimsSet.builder()
            .issuer("self").issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(60* 15))
            .subject(authentication.getName())
            .claim("scope", createScope(authentication)).build();
            
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    private String  createScope(Authentication authentication) {
        var scopes = authentication.getAuthorities().stream()
        .map(a -> a.getAuthority())
        .collect(Collectors.joining(" "));

        return scopes;
    }
    
}
