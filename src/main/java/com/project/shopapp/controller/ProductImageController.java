package com.project.shopapp.controller;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.shopapp.dtos.response.ProductImageResponse;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.mapper.ProductImageMapper;
import com.project.shopapp.service.IProductImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final IProductImageService productImageService;
    private final ProductImageMapper mapper;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getImageByProduct(@PathVariable("productId") Long productId) {
        List<ProductImageResponse> images = productImageService.getActiveImagesByProduct(productId).stream()
                .map(productImage -> mapper.toResponse(productImage)).toList();
        return ResponseEntity.ok(images);
    }

    @PostMapping(value = "/uploads/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("productId") Long productId,
            @RequestParam List<MultipartFile> files) {
        try {
            if (files == null || files.isEmpty()) {
                throw new InvalidParamException("No files uploaded");
            }
            if (files.size() > 5) {
                throw new InvalidParamException("Cannot upload more than 5 files per request");
            }
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(mapper.toListResponse(productImageService.uploadProductImages(productId, files)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductImage(@PathVariable Long id) {
        productImageService.deleteProductImage(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<String> forceDelete(@PathVariable Long id) {
        productImageService.forceDeleteProductImage(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}/forceall")
    public ResponseEntity<String> forceDeleteByProduct(@PathVariable("groupId") Long groupId) {
        productImageService.forceDeleteImagesByProduct(groupId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<String> restoreProductImage(@PathVariable Long id) {
        return ResponseEntity.ok("Image restore successfully" + productImageService.restoreProductImage(id));
    }
}
