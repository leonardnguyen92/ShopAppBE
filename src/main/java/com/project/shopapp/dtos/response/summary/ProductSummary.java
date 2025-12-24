package com.project.shopapp.dtos.response.summary;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Builder
@AllArgsConstructor
public class ProductSummary {

    private String name;
    private String description;
    private BigDecimal price;
}
