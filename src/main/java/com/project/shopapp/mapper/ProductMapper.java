package com.project.shopapp.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.project.shopapp.dtos.response.ProductResponse;
import com.project.shopapp.entity.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductResponse toResponse(Product product) {
        if (product == null)
            return null;
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setCategoryId(product.getCategory().getId());
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }

    public List<ProductResponse> toListResponse(List<Product> listProduct) {
        if (listProduct == null || listProduct.isEmpty())
            return List.of();
        return listProduct.stream().map(this::toResponse).toList();
    }
}
