package com.example.springsecuritydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.*;
import java.util.*;
import java.util.stream.*;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.context.annotation.*;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.access.prepost.PreAuthorize;

@SpringBootApplication
public class SpringSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}

}

// security configurations

@Configuration
@EnableMethodSecurity
class SecurityConfig {

	//authentication

		@Bean
	public InMemoryUserDetailsManager usrDtlMng() {
	
		var user = User.builder().username("myusr")
					.password("dummy")
					.passwordEncoder(str -> passwordEncoder().encode(str))
					.roles("USER").build();
	
		var admin = User.builder().username("admin")
					.password("dummy")
					.passwordEncoder(str -> passwordEncoder().encode(str))
					.roles("USER","ADMIN").build();
					
		return new InMemoryUserDetailsManager(user,admin);
	}

	@Bean
	public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
		return http
			.csrf( c -> c.disable() )
			.authorizeHttpRequests( auth -> auth.requestMatchers("/api/products/new").permitAll() )
			.authorizeHttpRequests( auth -> auth.requestMatchers("/api/products/**").authenticated() )
			.sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.httpBasic(Customizer.withDefaults())
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}

// data model layer


@Entity
@NoArgsConstructor
@Data
class Product{

	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private int quantity;
	private double price;

	public Product(String name, int quantity, double price) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
}

record ProductMask(String name, int quantity, double price) {}

// repository layer

interface ProductRepo extends JpaRepository<Product, Integer> {
}

// controller layer

@RestController
@RequestMapping("/api")
class ProductController {

	private final ProductRepo repo;

	public ProductController(ProductRepo repo) {
		this.repo = repo;
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("products/all")
	public List<Product> allProducts() {
		return repo.findAll();
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("products/find/{id}")
	public Product findAProduct(@PathVariable Integer id) {
		return repo.findById(id).orElseThrow( () -> new RuntimeException("product not found") );
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("products/new")
	public Product createProduct(@RequestBody ProductMask product) {
		if(product.name() != null) {
			Product pdt = new Product(product.name(), product.quantity(), product.price());
			return repo.save(pdt);
		}else {
			throw new RuntimeException("null values are not allowed!!!");
		}
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("products/update/{id}")
	public Product updateProduct(@PathVariable Integer id, @RequestBody ProductMask product) {
		Product newPdct = repo.findById(id).orElseThrow( () -> new RuntimeException("product not found") );

		newPdct.setName(product.name());
		newPdct.setQuantity(product.quantity());
		newPdct.setPrice(product.price());

		return repo.save(newPdct);
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@DeleteMapping("products/delete/{id}")
	public void deleteProduct(@PathVariable Integer id) {
		repo.deleteById(id);
	}

}
