/**
 * CategoryRepository.java
 * com.project.shopapp.repository
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopapp.entity.Category;

/**
 * Repository for Category entity.
 * Provides CRUD operations and database access logic for categories.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
