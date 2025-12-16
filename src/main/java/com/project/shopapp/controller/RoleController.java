/**
 * RoleController.java
 * com.project.shopapp.controller
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.dtos.RoleDTO;
import com.project.shopapp.entity.Role;
import com.project.shopapp.service.IRoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/roles")
public class RoleController {

	private final IRoleService roleService;

	@PostMapping
	public ResponseEntity<?> createRole(@Valid @RequestBody RoleDTO roleDTO, BindingResult result) {
		try {
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			roleService.createRole(roleDTO);
			return ResponseEntity.ok(roleDTO);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllRoles() {
		List<Role> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
		try {
			Role updateRole = roleService.updateRole(id, roleDTO);
			return ResponseEntity.ok(updateRole);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRole(@PathVariable Long id) {
		try {
			roleService.deleteRole(id);
			return ResponseEntity.ok(String.format("Delete Role with ID = %d successfully", id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
