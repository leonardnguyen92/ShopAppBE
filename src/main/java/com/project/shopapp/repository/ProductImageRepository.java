package com.project.shopapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopapp.entity.ProductImage;

/**
 * 
 */
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    /**
     * 
     * @param productId
     * @return
     */
    List<ProductImage> findByProductId(Long productId);

    /**
     * 
     * @param productId
     * @return
     */
    int countByProductId(Long productId);
}
