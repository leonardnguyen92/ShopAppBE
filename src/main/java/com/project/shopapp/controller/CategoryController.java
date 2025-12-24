package com.project.shopapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.response.CategoryResponse;
import com.project.shopapp.entity.Category;
import com.project.shopapp.mapper.CategoryMapper;
import com.project.shopapp.service.ICategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final ICategoryService categoryService;
	private final CategoryMapper categoryMapper;

	@GetMapping("")
	public ResponseEntity<?> getAllCategories(@RequestParam int page, @RequestParam int limit) {
		List<Category> categories = categoryService.getAllCategory();
		return ResponseEntity.ok(categories);
	}

	@PostMapping("")
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
		try {
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			Category category = categoryService.createCategory(categoryDTO);
			CategoryResponse response = categoryMapper.toResponse(category);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
		try {
			Category updateCategory = categoryService.updateCategory(id, categoryDTO);
			return ResponseEntity.ok(updateCategory);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}/force")
	public ResponseEntity<String> forceDeleteCategory(@PathVariable Long id) {
		try {
			categoryService.forceDeleteCategory(id);
			return ResponseEntity.ok(String.format("Delete category with ID = %d successfully", id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PatchMapping("/{id}/enable")
	public ResponseEntity<?> enableCategory(@PathVariable Long id) {
		try {
			Category existingCategory = categoryService.enableCategory(id);
			return ResponseEntity.ok(existingCategory);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PatchMapping("/{id}/disable")
	public ResponseEntity<?> disableCategory(@PathVariable Long id) {
		try {
			Category existingCategory = categoryService.disableCategory(id);
			return ResponseEntity.ok(existingCategory);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
