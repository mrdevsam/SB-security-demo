package com.example.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.JdbcUserDetailsManager;


@Configuration
public class BasicAuthSecurityConfig {

	@Bean
	public DataSource dSource() {
		return new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2)
					.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
					.build();
	}
	
	@Bean
	public UserDetailsService usrDtlServ(DataSource source) {

		var user = User.builder().username("myusr")
						//.password("{noop}dummy")
						.password("dummy")
						.passwordEncoder(str -> passwordEncoder().encode(str))
						.roles("USER").build();

		var admin = User.builder().username("admin")
						//.password("{noop}dummy")
						.password("dummy")
						.passwordEncoder(str -> passwordEncoder().encode(str))
						.roles("USER","ADMIN").build();

		//return new InMemoryUserDetailsManager(user,admin);

		var jdbcUserdetailsManager = new JdbcUserDetailsManager(source);
		jdbcUserdetailsManager.createUser(admin);
		jdbcUserdetailsManager.createUser(user);
		return jdbcUserdetailsManager;
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
