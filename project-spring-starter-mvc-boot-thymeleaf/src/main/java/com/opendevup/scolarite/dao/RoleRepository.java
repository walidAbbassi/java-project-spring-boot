/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opendevup.scolarite.entities.Role;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	/**
	 * find role by role type (ADMIN/USER)
	 * @param role
	 * @return
	 */
	Role findByRole(String role);

}