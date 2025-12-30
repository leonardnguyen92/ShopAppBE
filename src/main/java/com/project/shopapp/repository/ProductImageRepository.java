package com.project.shopapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.enums.ProductImageStatus;

import jakarta.persistence.LockModeType;

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
        int countByProductIdAndStatus(Long productId, ProductImageStatus status);

        /**
         * 
         * @param productId
         * @param status
         * @return
         */
        List<ProductImage> findByProductIdAndStatus(Long productId, ProductImageStatus status);

        /**
         * 
         * @param id
         * @return
         */
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT p FROM ProductImage p WHERE p.id = :id")
        Optional<ProductImage> findByIdForUpdate(@Param("id") long id);

        @Query("SELECT p FROM ProductImage p " +
                        "WHERE p.product.id = :productId " +
                        "AND p.status = :status " +
                        "AND p.id <> :deletedId " +
                        "ORDER BY p.id ASC")
        List<ProductImage> findNextImageForThumbnail(
                        @Param("productId") Long productId,
                        @Param("status") ProductImageStatus status,
                        @Param("deletedId") Long deletedId,
                        Pageable pageable);
}
