package com.example.springsecuritydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}

}

// data model layer

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
class UserInfo{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private String email;
	private String password;
	private String roles;
}

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
class Product{

	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private int qnty;
	private double price;
}

