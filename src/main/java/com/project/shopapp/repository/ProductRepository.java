package com.project.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopapp.entity.Product;

/**
 * 
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 
     * @param name
     * @return
     */
    boolean existsByName(String name);
}
