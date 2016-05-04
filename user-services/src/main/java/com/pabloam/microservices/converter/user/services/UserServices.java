package com.pabloam.microservices.converter.user.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.pabloam.microservices.converter.user.model.User;

public interface UserServices extends UserDetailsService {

	/**
	 * Registers the given user in the database
	 * 
	 * @param user
	 * @return
	 */
	User register(User user);

}
