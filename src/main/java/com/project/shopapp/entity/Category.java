/**
 * 
 */
package com.project.shopapp.entity;

import com.project.shopapp.enums.CategoryStatus;

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

	@Enumerated(EnumType.STRING)
	@Column(length = 30, nullable = false)
	private CategoryStatus status;
}
