/**
 * DataNotFoundException.java
 * com.project.shopapp.exceptions
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.exceptions;

/**
 * Exception thrown when requested data is not found.</br>
 * Thrown when an entity cannot be found in the database.</br>
 *
 * Ngoại lệ được ném ra khi không tìm thấy dữ liệu tương ứng trong hệ thống.
 */
public class DataNotFoundException extends RuntimeException{
	public DataNotFoundException (String message) {
		super(message);
	}
}
