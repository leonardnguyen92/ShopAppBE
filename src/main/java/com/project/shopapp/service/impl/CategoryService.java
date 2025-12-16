/**
 * CategoryService.java
 * com.project.shopapp.service
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.entity.Category;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.repository.CategoryRepository;
import com.project.shopapp.service.ICategoryService;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public Category createCategory(CategoryDTO categoryDTO) {
		Category newCategory = Category.builder().name(categoryDTO.getName()).build();
		newCategory.setActive(true);
		return categoryRepository.save(newCategory);
	}

	@Override
	public Category getCategoryById(long id){
		return categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Category not found with id: " + id));
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> listCategory = categoryRepository.findAll();
		return listCategory;
	}

	@Override
	public Category updateCategory(long categoryId, CategoryDTO categoryDTO) {
		Category existingCategory = getCategoryById(categoryId);
		existingCategory.setName(categoryDTO.getName());
		existingCategory.setActive(true);
		categoryRepository.save(existingCategory);
		return existingCategory;
	}

	@Override
	public void deleteCategory(long id) {
		Category category = categoryRepository.findById(id).orElse(null);
		if(category != null) {
			category.setActive(false);
			categoryRepository.save(category);
		}
	}

}
