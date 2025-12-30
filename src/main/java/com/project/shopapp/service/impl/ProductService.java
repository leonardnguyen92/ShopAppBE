package com.project.shopapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.entity.Category;
import com.project.shopapp.entity.Product;
import com.project.shopapp.enums.CategoryStatus;
import com.project.shopapp.enums.ProductStatus;
import com.project.shopapp.exceptions.BusinessException;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.repository.CategoryRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.service.IProductService;
import com.project.shopapp.utils.PriceUtils;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

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
                .status(ProductStatus.ACTIVE)
                .category(existsingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public void deleteProduct(long id) {
        Product existingProduct = getProductById(id);
        existingProduct.setStatus(ProductStatus.DELETED_BY_ADMIN);
        productRepository.save(existingProduct);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        // Lấy danh sách sản phẩm theo trang(page) và giới hạn số sản phẩm hiển thị
        // trong 1 trang (limit)
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllProductsForUser(Pageable pageable) {
        return productRepository.findByStatusAndCategory_Status(ProductStatus.ACTIVE, CategoryStatus.ACTIVE, pageable);
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product cannot find with id: " + id));
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO) {
        Product existingProduct = getProductById(id);
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Category cannot find with id: " + productDTO.getCategoryId()));
        existingProduct.setName(productDTO.getName());
        existingProduct.setCategory(existingCategory);
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        existingProduct.setDescription(productDTO.getDescription());
        return existingProduct;

    }

    @Override
    @Transactional
    public Product restoreProduct(long id) {
        Product existingProduct = productRepository
                .findByIdAndStatusAndCategory_Status(id, ProductStatus.DELETED_BY_ADMIN, CategoryStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(
                        "Product " + id + " does not exist or is not DELETED_BY_ADMIN"));
        existingProduct.setStatus(ProductStatus.INACTIVE);
        return existingProduct;
    }

    @Override
    @Transactional
    public void forceDeleteProduct(long id) {
        Product existingProduct = productRepository.findByIdAndStatus(id, ProductStatus.DELETED_BY_ADMIN)
                .orElseThrow(() -> new BusinessException(
                        "Product " + id + " does not exist or is not DELETED_BY_ADMIN"));

        productRepository.delete(existingProduct);
    }

    @Override
    @Transactional
    public Product enableProduct(long id) {
        Product existingProduct = productRepository
                .findByIdAndStatusAndCategory_Status(id, ProductStatus.INACTIVE, CategoryStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("Product " + id + " does not exist or is not INACTIVE"));
        existingProduct.setStatus(ProductStatus.ACTIVE);
        return existingProduct;
    }

    @Override
    @Transactional
    public Product disableProduct(long id) {
        Product existingProduct = productRepository.findByIdAndStatus(id, ProductStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(
                        "Product " + id + " does not exist or is not ACTIVE"));
        existingProduct.setStatus(ProductStatus.INACTIVE);
        return existingProduct;
    }

    @Override
    @Transactional
    public void generateFakeProducts(int totalRecords) {
        Faker faker = new Faker();
        List<Product> batch = new ArrayList<>();
        List<Category> category = categoryRepository.findByStatus(CategoryStatus.ACTIVE);
        if (category == null || category.isEmpty()) {
            throw new DataNotFoundException("Cannot found Category");
        }
        int batchSize = 500;
        Random random = new Random();
        int created = 0;
        while (created < totalRecords) {
            long fakePrice = faker.number().numberBetween(10000, 100000000);
            Product product = Product.builder()
                    .name(faker.commerce().productName())
                    .price(PriceUtils.roundPrice(fakePrice))
                    .thumbnail(null)
                    .description(faker.lorem().sentence())
                    .status(ProductStatus.ACTIVE)
                    .category(category.get(random.nextInt(category.size())))
                    .build();
            batch.add(product);
            created++;
            if (batch.size() >= batchSize) {
                productRepository.saveAll(batch);
                productRepository.flush();
                batch.clear();
                System.out.println("Inserted " + created + " products");
            }

        }
        if (!batch.isEmpty()) {
            productRepository.saveAll(batch);
            productRepository.flush();
        }
    }

}
