package com.project.shopapp.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.enums.ProductImageStatus;
import com.project.shopapp.enums.ProductStatus;
import com.project.shopapp.exceptions.BusinessException;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.repository.ProductImageRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.service.IFileService;
import com.project.shopapp.service.IProductImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final IFileService fileService;

    @Override
    public List<ProductImage> getActiveImagesByProduct(long productId) {
        return productImageRepository.findByProductIdAndStatus(productId,
                ProductImageStatus.ACTIVE);
    }

    @Override
    @Transactional
    public List<ProductImage> uploadProductImages(long productId, List<MultipartFile> files) throws IOException {
        Product existingProduct = productRepository.findByIdAndStatus(productId, ProductStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(
                        "Product with id = " + productId + " does not exist or is not ACTIVE."));
        int currentCount = productImageRepository.countByProductIdAndStatus(productId, ProductImageStatus.ACTIVE);
        if ((currentCount + files.size()) > 5) {
            throw new InvalidParamException("Total images cannot exceed 5");
        }
        List<ProductImage> newImages = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = fileService.storeImageFile(file);
            try {
                ProductImage savedImage = productImageRepository.save(ProductImage.builder()
                        .product(existingProduct)
                        .imageUrl(fileName)
                        .status(ProductImageStatus.ACTIVE)
                        .build());
                newImages.add(savedImage);
            } catch (Exception e) {
                fileService.deleteImageFile(fileName);
                throw new FileUploadException("Cannot save file " + fileName);
            }

        }
        // Nếu product chưa có thumbnail thì gán ảnh được upload đầu tiên vào thumbnail
        if ((existingProduct.getThumbnail() == null || existingProduct.getThumbnail().isEmpty())
                && !newImages.isEmpty()) {
            existingProduct.setThumbnail(newImages.get(0).getImageUrl());
        }
        return newImages;
    }

    @Override
    @Transactional
    public void deleteProductImage(long id) {
        ProductImage existingProductImage = getImageById(id);
        Product product = existingProductImage.getProduct();
        setThumbnailByDeletedImage(existingProductImage, product);
        existingProductImage.setStatus(ProductImageStatus.DELETED);
        productImageRepository.save(existingProductImage);
    }

    @Override
    @Transactional
    public void forceDeleteProductImage(long id) {
        ProductImage existingProductImage = getImageById(id);
        Product product = existingProductImage.getProduct();
        setThumbnailByDeletedImage(existingProductImage, product);
        if (checkDeleteImage(existingProductImage)) {
            productImageRepository.delete(existingProductImage);
        }

    }

    @Override
    @Transactional
    public ProductImage restoreProductImage(long id) {
        ProductImage existingProductImage = productImageRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Image with id: " + id));
        Product product = existingProductImage.getProduct();
        if (product == null) {
            throw new DataNotFoundException("Cannot find Product with image id: " + id);
        }
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new InvalidParamException("Cannot restore image: Product is not ACTIVE");
        }
        int countImageActive = productImageRepository
                .countByProductIdAndStatus(existingProductImage.getProduct().getId(), ProductImageStatus.ACTIVE);
        if (countImageActive >= 5) {
            throw new InvalidParamException("Cannot restore image: Total active images reached 5.");
        }
        existingProductImage.setStatus(ProductImageStatus.ACTIVE);
        ProductImage restoreImage = productImageRepository.save(existingProductImage);
        if (product.getThumbnail() == null || product.getThumbnail().isEmpty()) {
            product.setThumbnail(restoreImage.getImageUrl());
        }
        return restoreImage;
    }

    @Override
    @Transactional
    public void forceDeleteImagesByProduct(long productId) {
        List<ProductImage> images = productImageRepository.findByProductId(productId);
        if (images.isEmpty()) {
            throw new DataNotFoundException("No images found for product id: " + productId);
        }

        Product product = images.get(0).getProduct();
        if (product == null) {
            throw new DataNotFoundException("Product not found for images of product id: " + productId);
        }
        for (ProductImage image : images) {
            try {
                if (checkDeleteImage(image)) {
                    productImageRepository.delete(image);
                }
            } catch (RuntimeException e) {
                System.err.println("Failed to delete image id=" + image.getId() + ": " + e.getMessage());
            }
        }
        product.setThumbnail(null);
        productRepository.save(product);
    }

    @Override
    public ProductImage getImageById(long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Image with id: " + id));
    }

    @Override
    public List<ProductImage> getImagesByProduct(long productId, ProductImageStatus status) {
        return productImageRepository.findByProductIdAndStatus(productId, status);
    }

    private boolean checkDeleteImage(ProductImage existingProductImage) {
        String imageUrl = existingProductImage.getImageUrl();
        boolean checkDeleted = fileService.deleteImageFile(imageUrl);
        if (!checkDeleted) {
            throw new RuntimeException("Cannot delete image file: " + imageUrl);
        }
        return checkDeleted;
    }

    private void setThumbnailByDeletedImage(ProductImage deletedImage, Product product) {
        if (product.getThumbnail() != null && product.getThumbnail().equals(deletedImage.getImageUrl())) {
            Pageable limitOne = PageRequest.of(0, 1);
            List<ProductImage> images = productImageRepository.findNextImageForThumbnail(product.getId(),
                    ProductImageStatus.ACTIVE, deletedImage.getId(), limitOne);
            if (!images.isEmpty()) {
                product.setThumbnail(images.get(0).getImageUrl());
            } else {
                product.setThumbnail(null);
            }
        }
    }

}
