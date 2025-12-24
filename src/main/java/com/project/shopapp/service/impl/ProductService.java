package com.project.shopapp.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.dtos.response.ProductImageResponse;
import com.project.shopapp.dtos.response.ProductResponse;
import com.project.shopapp.dtos.response.summary.ProductSummary;
import com.project.shopapp.entity.Category;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.repository.CategoryRepository;
import com.project.shopapp.repository.ProductImageRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.service.IProductService;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Long categoryId = productDTO.getCategoryId();
        if (categoryId == null || categoryId <= 0) {
            throw new InvalidParamException("Category ID is required and must be > 0");
        }
        Category existsingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new DataNotFoundException("Category not found with id: " + categoryId));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existsingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) {
        Product existsingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));
        ProductImage newProductImage = ProductImage.builder().product(existsingProduct)
                .imageUrl(productImageDTO.getImageUrl()).build();
        int size = productImageRepository.countByProductId(productId);
        if (size > 5) {
            throw new InvalidParamException("Number of images must be <= 5");
        }
        return productImageRepository.save(newProductImage);
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        // Lấy danh sách sản phẩm theo trang(page) và giới hạn số sản phẩm hiển thị
        // trong 1 trang (limit)
        return productRepository.findAll(pageable).map(ProductResponse::fromProduct);
    }

    @Override
    public Page<ProductResponse> getAllProductsForUser(Pageable pageable) {
        return productRepository.findAllActiveProducts(pageable).map(ProductResponse::fromProduct);
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product cannot find with id: " + id));
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) {
        Product existsingProduct = getProductById(id);
        if (existsingProduct != null) {
            Category existsingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                    () -> new DataNotFoundException("Category cannot find with id: " + productDTO.getCategoryId()));
            existsingProduct.setName(productDTO.getName());
            existsingProduct.setCategory(existsingCategory);
            existsingProduct.setPrice(productDTO.getPrice());
            existsingProduct.setThumbnail(productDTO.getThumbnail());
            existsingProduct.setDescription(productDTO.getDescription());
            return productRepository.save(existsingProduct);
        } else {
            throw new DataNotFoundException("Product not found with id: " + id);
        }

    }

    @Transactional
    @Override
    public List<ProductImageResponse> uploadProductImages(long productId, List<MultipartFile> files)
            throws IOException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with id: " + productId));
        int currentCount = productImageRepository.countByProductId(productId);
        if ((currentCount + files.size()) > 5) {
            throw new InvalidParamException("Total images cannot exceed 5");
        }
        ProductSummary productSummary = ProductSummary.builder()
                .name(existingProduct.getName())
                .description("")
                .price(null)
                .build();
        List<ProductImageResponse> newImages = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = storeFile(file);
            ProductImage savedImage = productImageRepository.save(ProductImage.builder()
                    .product(existingProduct)
                    .imageUrl(fileName)
                    .build());
            newImages.add(ProductImageResponse.builder()
                    .id(savedImage.getId())
                    .imageUrl(savedImage.getImageUrl())
                    .productSummary(productSummary)
                    .build());
        }

        // Nếu product chưa có thumbnail thì gán ảnh được upload đầu tiên vào thumbnail
        if ((existingProduct.getThumbnail() == null || existingProduct.getThumbnail().isEmpty())
                && !newImages.isEmpty()) {
            existingProduct.setThumbnail(newImages.get(0).getImageUrl());
            productRepository.save(existingProduct);
        }
        return newImages;
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid Image Format");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        // Đường dẫn thư mục bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentFile = file.getContentType();
        return contentFile != null && contentFile.startsWith("image/");
    }

}
