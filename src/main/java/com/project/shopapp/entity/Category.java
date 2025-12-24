/**
 * 
 */
package com.project.shopapp.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing the Category table in the database
 */
@Entity
@Table(name = "categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;
}
