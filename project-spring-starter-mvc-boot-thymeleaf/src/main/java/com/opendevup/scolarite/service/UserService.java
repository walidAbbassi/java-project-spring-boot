/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.service;

import com.opendevup.scolarite.entities.User;

public interface UserService {
	
	/**
	 * Find user with email value from database
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email);
	
	/**
	 * Find user with username value from database
	 * @param username
	 * @return
	 */
	public User findUserByUsername(String username);
	
	/**
	 * Save user in database
	 * @param user
	 */
	public void saveUser(User user, String roleType);
	
}