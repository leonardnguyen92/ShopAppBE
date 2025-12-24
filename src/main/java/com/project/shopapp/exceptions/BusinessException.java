package com.project.shopapp.exceptions;

/**
 * Exception thrown when requested data is not found.</br>
 * Thrown when an entity cannot be found in the database.</br>
 *
 * Ngoại lệ được ném ra khi xảy ra lỗi nghiệp vụ trong hệ thống.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
