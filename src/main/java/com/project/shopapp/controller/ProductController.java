package com.project.shopapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.service.IProductService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PostMapping("generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts() {
        try {
            Faker faker = new Faker();
            for (int i = 0; i < 10000; i++) {
                String productName = faker.commerce().productName();
                if (productService.existsByName(productName)) {
                    continue;
                }
                ProductDTO productDTO = ProductDTO.builder()
                        .name(productName)
                        .price(BigDecimal.valueOf(faker.number().numberBetween(10, 100000000)))
                        .thumbnail("")
                        .description(faker.lorem().sentence())
                        .categoryId((long) faker.number().numberBetween(1, 7))
                        .build();
                productService.createProduct(productDTO);
            }
            return ResponseEntity.ok("create success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,
            @RequestParam List<MultipartFile> files) {
        try {
            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body("No files uploaded");
            }
            if (files.size() > 5) {
                return ResponseEntity.badRequest().body("Cannot upload more than 5 files per request");
            }
            ;
            List<ProductImage> uploadedImages = productService.uploadProductImages(productId, files);
            return ResponseEntity.ok(uploadedImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
