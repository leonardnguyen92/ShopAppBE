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
import org.springframework.transaction.annotation.Transactional;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.entity.Category;
import com.project.shopapp.entity.Product;
import com.project.shopapp.exceptions.BusinessException;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.repository.CategoryRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.service.ICategoryService;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;

	@Override
	public Category createCategory(CategoryDTO categoryDTO) {
		Category newCategory = Category.builder().name(categoryDTO.getName()).build();
		newCategory.setActive(true);
		return categoryRepository.save(newCategory);
	}

	@Override
	public Category getCategoryById(long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Category not found with id: " + id));
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> listCategory = categoryRepository.findAll();
		return listCategory;
	}

	@Override
	public Category updateCategory(long id, CategoryDTO categoryDTO) {
		Category existingCategory = getCategoryById(id);
		existingCategory.setName(categoryDTO.getName());
		categoryRepository.save(existingCategory);
		return existingCategory;
	}

	@Override
	@Transactional
	public void forceDeleteCategory(long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Category not found with id: " + id));
		if (productRepository.existsByCategoryId(id)) {
			throw new BusinessException("Cannot force delete category because it still has products");
		}
		categoryRepository.delete(category);
	}

	@Override
	@Transactional
	public Category enableCategory(long id) {
		Category existingCategory = getCategoryById(id);
		existingCategory.setActive(true);
		List<Product> products = productRepository.findByCategoryId(id);
		for (Product product : products) {
			if (!product.isDeletedByAdmin() && !product.isActive()) {
				product.setActive(true);
			}
		}
		productRepository.saveAll(products);
		categoryRepository.save(existingCategory);
		return existingCategory;
	}

	@Override
	@Transactional
	public Category disableCategory(long id) {
		Category existingCategory = getCategoryById(id);
		existingCategory.setActive(false);
		List<Product> products = productRepository.findByCategoryId(id);
		for (Product product : products) {
			product.setActive(false);

		}
		productRepository.saveAll(products);
		categoryRepository.save(existingCategory);
		return existingCategory;
	}

}
