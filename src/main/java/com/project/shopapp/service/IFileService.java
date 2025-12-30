package com.project.shopapp.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    /**
     * 
     * @param file
     * @return
     */
    boolean isImageFile(MultipartFile file);

    /**
     * 
     * @param file
     * @return
     * @throws IOException
     */
    String storeImageFile(MultipartFile file) throws IOException;

    /**
     * 
     * @param filePath
     * @return
     */
    boolean deleteImageFile(String filePath);

}
