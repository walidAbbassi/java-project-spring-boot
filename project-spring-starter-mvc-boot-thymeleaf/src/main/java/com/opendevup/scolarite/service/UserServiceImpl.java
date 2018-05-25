/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.opendevup.scolarite.dao.RoleRepository;
import com.opendevup.scolarite.dao.UserRepository;
import com.opendevup.scolarite.entities.Role;
import com.opendevup.scolarite.entities.User;


@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/* (non-Javadoc)
	 * @see com.opendevup.scolarite.service.UserService#findUserByEmail(java.lang.String)
	 */
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	/* (non-Javadoc)
	 * @see com.opendevup.scolarite.service.UserService#findUserByUsername(java.lang.String)
	 */
	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/* (non-Javadoc)
	 * @see com.opendevup.scolarite.service.UserService#saveUser(com.opendevup.scolarite.entities.User)
	 */
	@Override
	public void saveUser(User user, String roleType) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole(roleType);
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

}