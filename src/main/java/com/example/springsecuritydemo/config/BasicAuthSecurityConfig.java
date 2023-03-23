package com.example.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BasicAuthSecurityConfig {

	@Bean
	public UserDetailsService usrDtlServ() {

		var user = User.builder().username("myusr")
						.password("{noop}dummy")
						.roles("USER").build();

		var admin = User.builder().username("admin")
						.password("{noop}dummy")
						.roles("USER","ADMIN").build();

		return new InMemoryUserDetailsManager(user,admin);
	}
}
