package com.project.shopapp.dtos.response;

import com.project.shopapp.enums.CategoryStatus;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private CategoryStatus status;
}
