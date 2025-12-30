package com.project.shopapp.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.project.shopapp.dtos.response.CategoryResponse;
import com.project.shopapp.entity.Category;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper modelMapper;

    public CategoryResponse toResponse(Category category) {
        if (category == null)
            return null;
        return modelMapper.map(category, CategoryResponse.class);
    }

    public List<CategoryResponse> toListResponse(List<Category> listCategory) {
        if (listCategory == null || listCategory.isEmpty())
            return List.of();
        return listCategory.stream().map(this::toResponse).toList();
    }
}
