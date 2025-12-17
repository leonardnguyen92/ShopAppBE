package com.project.shopapp.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.dtos.response.ProductResponse;
import com.project.shopapp.entity.Product;
import com.project.shopapp.entity.ProductImage;

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
    Page<ProductResponse> getAllProducts(Pageable pageable);

    /**
     * 
     * @param id
     * @param productDTO
     * @return
     */
    Product updateProduct(long id, ProductDTO productDTO);

    /**
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
     * 
     * @param productId
     * @param productImageDTO
     * @return
     */
    ProductImage createProductImage(long productId, ProductImageDTO productImageDTO);

    /**
     * 
     * @param productId
     * @param file
     * @return
     * @throws IOException 
     */
    List<ProductImage> uploadProductImages(long productId, List<MultipartFile> files) throws IOException;
}
