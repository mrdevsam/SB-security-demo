package com.example.springsecuritydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.*;
import java.util.*;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;

@SpringBootApplication
public class SpringSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}

}

// data model layer

@Entity
@NoArgsConstructor
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

interface UserInfoRepo extends JpaRepository<UserInfo, Integer> {
	Optional<UserInfo> findByName(String username);
}

interface ProductRepo extends JpaRepository<Product, Integer> {
}

// controller layer

@RestController
@RequestMapping("/api/products")
class ProductController {

	private final ProductRepo repo;

	public ProductController(ProductRepo repo) {
		this.repo = repo;
	}

	@GetMapping("/all")
	public List<Product> allProducts() {
		return repo.findAll();
	}

	@GetMapping("/find/{id}")
	public Product findAProduct(@PathVariable Integer id) {
		return repo.findById(id).orElseThrow( () -> new RuntimeException("product not found") );
	}

	@PostMapping("/new")
	public Product createProduct(@RequestBody ProductMask product) {
		if(product.name() != null) {
			Product pdt = new Product(product.name(), product.quantity(), product.price());
			return repo.save(pdt);
		}else {
			throw new RuntimeException("null values are not allowed!!!");
		}
	}

	@PutMapping("/update/{id}")
	public Product updateProduct(@PathVariable Integer id, @RequestBody ProductMask product) {
		Product newPdct = repo.findById(id).orElseThrow( () -> new RuntimeException("product not found") );

		newPdct.setName(product.name());
		newPdct.setQuantity(product.quantity());
		newPdct.setPrice(product.price());

		return repo.save(newPdct);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteProduct(@PathVariable Integer id) {
		repo.deleteById(id);
	}
}
