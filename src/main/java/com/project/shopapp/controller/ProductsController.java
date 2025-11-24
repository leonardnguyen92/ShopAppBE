package com.project.shopapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.dtos.ProductDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

	@GetMapping("")
	public ResponseEntity<String> getAllProducts(@RequestParam int page, @RequestParam int limit) {
		return ResponseEntity.ok(String.format("getAllProducts with page = %d and limit = %d", page, limit));
	}

	@GetMapping("/{id}")
	public ResponseEntity<String> getProductWithId(@PathVariable Long id) {
		return ResponseEntity.ok(String.format("Product with id = %d load successfully", id));
	}

	@PostMapping("")
	public ResponseEntity<?> insertProduct(@Valid @RequestBody ProductDTO product, BindingResult result) {
		if (result.hasErrors()) {
			List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
			return ResponseEntity.badRequest().body(errorMessages);
		}
		return ResponseEntity.ok(String.format("Insert product with name %s successfully", product.getName()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable Long id) {
		return ResponseEntity.ok(String.format("Update product with id = %d successfully", id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
		return ResponseEntity.ok(String.format("Delete product with id = %d successfully", id));
	}
}
