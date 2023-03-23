package com.example.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(
			auth -> auth.anyRequest().authenticated()
		);

		http.sessionManagement(
			session -> session.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS
			)
		);

		//hhtp.formLogin();
		http.httpBasic();
		http.csrf().disable();
		return http.build();
	}
}
