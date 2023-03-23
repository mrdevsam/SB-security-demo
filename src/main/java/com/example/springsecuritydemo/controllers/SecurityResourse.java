package com.example.springsecuritydemo.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;


@RestController
public class SecurityResourse {

	@GetMapping("csrf-token-details")
	public CsrfToken showTokenDetails(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
}
