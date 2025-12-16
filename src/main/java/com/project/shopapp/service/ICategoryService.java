/**
 * ICategoryService.java
 * com.project.shopapp.service.impl
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.service;

import java.util.List;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.entity.Category;

/**
 * Category service interface. Defines business logic operations for Category.
 * 
 * Interface dịch vụ xử lý nghiệp vụ liên quan đến Category.
 */
public interface ICategoryService {
	/**
	 * Create a new category.</br>
	 * Tạo mới một category từ dữ liệu DTO.
	 * 
	 * @param categoryDTO data used to create category
	 * @return created Category entity
	 */
	Category createCategory(CategoryDTO categoryDTO);

	/**
	 * Get category by its ID.</br>
	 * Lấy thông tin category theo ID.
	 * 
	 * @param id category identifier
	 * @return Category entity
	 * @throws Exception if category not found
	 */
	Category getCategoryById(long id);

	/**
	 * Get all categories.</br>
	 * Lấy danh sách tất cả category.
	 *
	 * @return list of categories
	 */
	List<Category> getAllCategory();

	/**
	 * Update an existing category.</br>
	 * Cập nhật thông tin category theo ID.
	 *
	 * @param categoryId  category identifier
	 * @param categoryDTO updated category data
	 * @return updated Category entity
	 */
	Category updateCategory(long categoryId, CategoryDTO categoryDTO);

	/**
	 * Delete category by ID.</br>
	 * Xóa category theo ID.
	 *
	 * @param id category identifier
	 */
	void deleteCategory(long id);

}
