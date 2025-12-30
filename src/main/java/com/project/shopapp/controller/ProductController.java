package com.project.shopapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.response.ApiResponse;
import com.project.shopapp.dtos.response.ProductImageResponse;
import com.project.shopapp.dtos.response.ProductListResponse;
import com.project.shopapp.dtos.response.ProductResponse;
import com.project.shopapp.mapper.ProductMapper;
import com.project.shopapp.service.IProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * 
 */
@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final ProductMapper mapper;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam int page, @RequestParam int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest).map(mapper::toResponse);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder().products(products).totalPages(totalPages).build());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllProductsForUser(@RequestParam int page, @RequestParam int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProductsForUser(pageRequest).map(mapper::toResponse);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder().products(products).totalPages(totalPages).build());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @PathVariable ProductDTO productDTO,
            BindingResult result) {
        ApiResponse<ProductResponse> response = new ApiResponse<>();
        try {
            if (result.hasErrors()) {
                response.setSuccess(false);
                response.setErrors(result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList());
                return ResponseEntity.badRequest().body(response);
            }
            response.setSuccess(true);
            ProductResponse productResponse = mapper.toResponse(productService.createProduct(productDTO));
            response.setData(productResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductWithId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @PathVariable ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(String.format("Update product with id = %d successfully", id));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> enableProduct(@PathVariable Long id) {
        productService.enableProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(String.format("Delete product with id = %d successfully", id));
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<String> forceDeleteProduct(@PathVariable Long id) {
        productService.forceDeleteProduct(id);
        return ResponseEntity.ok(String.format("Force delete product with id = %d successfully.", id));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<?> restoreProduct(@PathVariable Long id) {
        productService.restoreProduct(id);
        return ResponseEntity.ok(String.format("Restore product with id = %d successfully.", id));
    }

    @PostMapping("fake-products")
    private ResponseEntity<String> generateFakeProducts() {
        productService.generateFakeProducts(3000);
        return ResponseEntity.status(HttpStatus.CREATED).body("create success");
    }

}
