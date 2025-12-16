/**
 * RoleService.java
 * com.project.shopapp.service.impl
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopapp.dtos.RoleDTO;
import com.project.shopapp.entity.Role;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.repository.RoleRepository;
import com.project.shopapp.service.IRoleService;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

	private final RoleRepository roleRepository;

	@Override
	public Role createRole(RoleDTO roleDTO) {
		Role newRole = Role.builder()
				.name(roleDTO.getName())
				.description(roleDTO.getDescription())
				.build();
		return roleRepository.save(newRole);
	}

	@Override
	public Role getRoleById(long id) {
		return roleRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Category not found with id: " + id));
	}

	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role updateRole(long roleId, RoleDTO roleDTO) {
		Role existingRole = getRoleById(roleId);
		existingRole.setName(roleDTO.getName());
		existingRole.setDescription(roleDTO.getDescription());
		roleRepository.save(existingRole);
		return existingRole;
	}

	@Override
	public void deleteRole(long id) {
		if (!roleRepository.existsById(id)) {
			throw new DataNotFoundException("Role not found");
		}
		roleRepository.deleteById(id);
	}

}
