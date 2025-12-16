/**
 * RoleRepository.java
 * com.project.shopapp.repository
 * shopapp
 * 2025-12-16
 * @author: Nguyen Hoan
 */
package com.project.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopapp.entity.Role;

/**
 * 
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
