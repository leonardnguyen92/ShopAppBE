package com.project.shopapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

  /**
   * 
   * @param categoryId
   * @return
   */
  boolean existsByCategoryId(Long categoryId);

  /**
   * 
   * @param categoryId
   * @return
   */
  List<Product> findByCategoryId(Long categoryId);

  /**
   * 
   * @param pageable
   * @return
   */
  @Query("""
          SELECT p FROM Product p
          WHERE p.isActive = true
            AND p.category.isActive = true
      """)
  Page<Product> findAllActiveProducts(Pageable pageable);
}
