package com.project.shopapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.project.shopapp.dtos.CategoryDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	@GetMapping("")
	public ResponseEntity<String> getAllCategories(@RequestParam int page, @RequestParam int limit) {
		return ResponseEntity.ok(String.format("This is getAllCategories with page = %d and limit = %d", page, limit));
	}

	@PostMapping("")
	public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO category, BindingResult result) {
		if (result.hasErrors()) {
			List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
			return ResponseEntity.badRequest().body(errorMessages);
		}
		return ResponseEntity.ok("This is insertCategory: Category's name: " + category.getName());
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateCategory(@PathVariable Long id) {
		return ResponseEntity.ok(String.format("This is updateCategory with id = %s", id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		return ResponseEntity.ok(String.format("Delete Category with id = %d", id));
	}
}
