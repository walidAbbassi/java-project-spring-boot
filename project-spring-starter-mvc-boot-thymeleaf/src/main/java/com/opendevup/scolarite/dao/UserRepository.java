/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opendevup.scolarite.entities.User;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	
	 /**
	 * find user by Email
	 * @param email
	 * @return
	 */
	User findByEmail(String email);
	
	
	 /**
	 * find user by username
	 * @param username
	 * @return
	 */
	User findByUsername(String username);
}