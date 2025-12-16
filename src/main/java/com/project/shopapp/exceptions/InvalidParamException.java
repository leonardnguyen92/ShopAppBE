/**
 * InvalidParamException.java
 * com.project.shopapp.exceptions
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.exceptions;

/**
 * Exception thrown when request parameters are invalid.</br>
 * Ngoại lệ được ném ra khi tham số đầu vào không hợp lệ.
 */
public class InvalidParamException extends RuntimeException {
	public InvalidParamException(String message) {
		super(message);
	}
}
