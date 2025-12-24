package com.project.shopapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.response.ProductImageResponse;
import com.project.shopapp.dtos.response.ProductListResponse;
import com.project.shopapp.dtos.response.ProductResponse;
import com.project.shopapp.service.IProductService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam int page, @RequestParam int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder().products(products).totalPages(totalPages).build());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllProductsForUser(@RequestParam int page, @RequestParam int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProductsForUser(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder().products(products).totalPages(totalPages).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductWithId(@PathVariable Long id) {
        return ResponseEntity.ok(String.format("Product with id = %d load successfully", id));
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
            List<ProductImageResponse> uploadedImages = productService.uploadProductImages(productId, files);
            return ResponseEntity.ok(uploadedImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(String.format("Update product with id = %d successfully", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(String.format("Delete product with id = %d successfully", id));
    }

    @PostMapping("generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() {
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

}
