package com.project.shopapp.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.project.shopapp.dtos.response.ProductImageResponse;
import com.project.shopapp.entity.ProductImage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {

    private final ModelMapper modelMapper;

    public ProductImageResponse toResponse(ProductImage productImage) {
        if (productImage == null)
            return null;
        return modelMapper.map(productImage, ProductImageResponse.class);
    }

    public List<ProductImageResponse> toListResponse(List<ProductImage> listImages) {
        if (listImages == null || listImages.isEmpty())
            return List.of();
        return listImages.stream().map(this::toResponse).toList();
    }
}
