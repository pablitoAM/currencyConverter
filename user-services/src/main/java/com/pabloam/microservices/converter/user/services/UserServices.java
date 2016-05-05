package com.pabloam.microservices.converter.user.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.pabloam.microservices.converter.user.model.User;

public interface UserServices extends UserDetailsService {

	/**
	 * Registers the given user in the database with the given default groups
	 * 
	 * @param user
	 * @param defaultUserGroups
	 * @return
	 */
	User register(User user, List<String> defaultUserGroups);

}
