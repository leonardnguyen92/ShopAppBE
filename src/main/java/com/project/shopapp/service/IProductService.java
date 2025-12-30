package com.project.shopapp.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.entity.Product;

/** */
public interface IProductService {

    /**
     * 
     * @param productDTO
     * @return
     */
    Product createProduct(ProductDTO productDTO);

    /**
     * 
     * @param id
     * @return
     */
    Product getProductById(long id);

    /**
     * 
     * @param pageable
     * @return
     */
    Page<Product> getAllProducts(Pageable pageable);

    /**
     * 
     * @param pageable
     * @return
     */
    Page<Product> getAllProductsForUser(Pageable pageable);

    /**
     * 
     * @param id
     * @param productDTO
     * @return
     */
    Product updateProduct(long id, ProductDTO productDTO);

    /**
     * DELETE:
     * - product.status ∈ {ACTIVE, INACTIVE}
     * → product.status = DELETED_BY_ADMIN
     * 
     * @param id
     */
    void deleteProduct(long id);

    /**
     * 
     * @param name
     * @return
     */
    boolean existsByName(String name);

    /**
     * RESTORE:
     * - product.status == DELETED_BY_ADMIN
     * - AND category.status == ACTIVE
     * → product.status = INACTIVE
     * 
     * @param id
     * @return
     */
    Product restoreProduct(long id);

    /**
     * FORCE DELETE:
     * - product.status == DELETED_BY_ADMIN
     * → remove DB + images + mappings
     * 
     * @param id
     */
    void forceDeleteProduct(long id);

    /**
     * ENABLE:
     * - product.status == INACTIVE
     * - AND category.status == ACTIVE
     * → product.status = ACTIVE
     * 
     * @param id
     * @return
     */
    Product enableProduct(long id);

    /**
     * DISABLE:
     * - product.status == ACTIVE
     * → product.status = INACTIVE
     * 
     * @param id
     * @return
     */
    Product disableProduct(long id);

    /**
     * 
     * @param total
     * @return
     */
    void generateFakeProducts(int totalRecords);
}
