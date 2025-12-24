package com.project.shopapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopapp.entity.Product;
import com.project.shopapp.enums.CategoryStatus;
import com.project.shopapp.enums.ProductStatus;

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
   * @param categoryId
   * @param status
   * @return
   */
  List<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status);

  /**
   * 
   * @param status
   * @param categoryStatus
   * @param pageable
   * @return
   */
  Page<Product> findByStatusAndCategory_Status(
      ProductStatus status,
      CategoryStatus categoryStatus,
      Pageable pageable);
}
