/**
 * RoleDTO.java
 * com.project.shopapp.dtos
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
	@NotBlank(message = "Role's name is required")
	private String name;
	
	private String description;
}
