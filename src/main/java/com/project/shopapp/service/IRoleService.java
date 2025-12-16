/**
 * IRoleService.java
 * com.project.shopapp.service
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.service;

import java.util.List;

import com.project.shopapp.dtos.RoleDTO;
import com.project.shopapp.entity.Role;

/**
 * 
 */
public interface IRoleService {
	/**
	 * Create a new role.</br>
	 * Tạo mới một role từ dữ liệu DTO.
	 * 
	 * @param roleDTO data used to create role
	 * @return created Role entity
	 */
	Role createRole(RoleDTO roleDTO);

	/**
	 * Get role by its ID.</br>
	 * Lấy thông tin role theo ID.
	 * 
	 * @param id role identifier
	 * @return Role entity
	 * @throws Exception if role not found
	 */
	Role getRoleById(long id);

	/**
	 * Get all roles.</br>
	 * Lấy danh sách tất cả role.
	 *
	 * @return list of roles
	 */
	List<Role> getAllRoles();

	/**
	 * Update an existing role.</br>
	 * Cập nhật thông tin role theo ID.
	 *
	 * @param roleId  role identifier
	 * @param roleDTO updated role data
	 * @return updated Role entity
	 */
	Role updateRole(long roleId, RoleDTO roleDTO);

	/**
	 * Delete role by ID.</br>
	 * Xóa role theo ID.
	 *
	 * @param id role identifier
	 */
	void deleteRole(long id);
}
