package com.project.shopapp.mapper;

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
        return modelMapper.map(category, CategoryResponse.class);
    }
}
