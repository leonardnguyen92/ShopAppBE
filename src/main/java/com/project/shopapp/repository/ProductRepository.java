package com.project.shopapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.project.shopapp.entity.Product;
import com.project.shopapp.enums.CategoryStatus;
import com.project.shopapp.enums.ProductStatus;

import jakarta.persistence.LockModeType;

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

  /**
   * Find product by productId and productStatus
   * 
   * @param id
   * @param deletedByAdmin
   * @return
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Product> findByIdAndStatus(Long id, ProductStatus productStatus);

  /**
   * Find a product by its ID, product status, and category status.
   * 
   * @param id             the ID of the product to search
   * @param productStatus  the status of the product (e.g., ACTIVE, INACTIVE,
   *                       DELETED_BY_ADMIN, etc.)
   * @param categoryStatus the status of the product's category (e.g., ACTIVE,
   *                       INACTIVE)
   * @return an {@link Optional} containing the {@link Product} if found;
   *         otherwise, an empty {@link Optional}
   */
  Optional<Product> findByIdAndStatusAndCategory_Status(Long id, ProductStatus productStatus,
      CategoryStatus categoryStatus);
}
