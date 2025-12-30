package com.project.shopapp.dtos;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	@NotBlank(message = "Title is required")
	@Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
	private String name;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
	@DecimalMax(value = "100000000.0", inclusive = true, message = "Price must be less than or equal to 100,000,000")
	private BigDecimal price;

	private String thumbnail;

	@NotBlank(message = "Description must not be empty")
	private String description;

	@NotNull(message = "Category Id is required")
	@Min(value = 1, message = "Category Id must be >= 1")
	@JsonProperty("category_id")
	private Long categoryId;

}
