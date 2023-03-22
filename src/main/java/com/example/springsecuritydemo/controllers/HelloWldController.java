package com.example.springsecuritydemo.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWldController{

	@GetMapping("/hlw-world")
	public String sayHello(){
		return "Hello World";
	}
}
