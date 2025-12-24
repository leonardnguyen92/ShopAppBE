package com.project.shopapp.entity;

import com.project.shopapp.enums.ProductImageStatus;

import jakarta.persistence.*;
import lombok.*;

/**
 * 
 */
@Entity
@Table(name = "product_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false, length = 300)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ProductImageStatus status;
}
