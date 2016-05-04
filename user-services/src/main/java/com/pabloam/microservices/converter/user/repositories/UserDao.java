/**
 * 
 */
package com.pabloam.microservices.converter.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pabloam.microservices.converter.user.model.User;

/**
 * @author PabloAM
 *
 */
public interface UserDao extends JpaRepository<User, Long> {

	/**
	 * Finds a user by the given email
	 * 
	 * @param email
	 * @param session
	 * @return
	 */
	@Query("select u from User u where u.email = ?1")
	User findByEmail(String email);

}
