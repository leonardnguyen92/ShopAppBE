package com.project.shopapp.dtos.response;

import com.project.shopapp.dtos.response.summary.ProductSummary;

import lombok.*;

/**
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageResponse {
    private Long id;
    private String imageUrl;
    private ProductSummary productSummary;
}