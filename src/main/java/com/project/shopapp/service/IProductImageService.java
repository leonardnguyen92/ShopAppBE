package com.project.shopapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.shopapp.entity.ProductImage;
import com.project.shopapp.enums.ProductImageStatus;

public interface IProductImageService {

    /**
     * 
     * @param productId
     * @return
     */
    List<ProductImage> getActiveImagesByProduct(long productId);

    /**
     * 
     * @param id
     * @return
     */
    ProductImage getImageById(long id);

    /**
     * 
     * @param productId
     * @param status
     * @return
     */
    List<ProductImage> getImagesByProduct(long productId, ProductImageStatus status);

    /**
     * 
     * @param productId
     * @param files
     * @return
     * @throws IOException
     */
    List<ProductImage> uploadProductImages(long productId, List<MultipartFile> files) throws IOException;

    /**
     * 
     * @param id
     */
    void deleteProductImage(long id);

    /**
     * 
     * @param id
     */
    void forceDeleteProductImage(long id);

    /**
     * 
     * @param id
     * @return
     */
    ProductImage restoreProductImage(long id);

    /**
     * 
     * @param productId
     */
    void forceDeleteImagesByProduct(long id);
}
