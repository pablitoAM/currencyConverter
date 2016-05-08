package com.pabloam.microservices.converter.user.services.impl;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.pabloam.microservices.converter.user.exception.RegistrationException;
import com.pabloam.microservices.converter.user.exception.UserServicesException;
import com.pabloam.microservices.converter.user.model.Group;
import com.pabloam.microservices.converter.user.model.User;
import com.pabloam.microservices.converter.user.repositories.GroupDao;
import com.pabloam.microservices.converter.user.repositories.UserDao;
import com.pabloam.microservices.converter.user.services.UserServices;

/**
 * @author Pablo
 *
 */
@Service
@Transactional
public class UserServicesImpl implements UserServices {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(UserServicesImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private GroupDao groupDao;

	/**
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, IllegalArgumentException {

		if (!StringUtils.hasText(email)) {
			throw new IllegalArgumentException("The email to verify cannot be null nor empty.");
		}

		User user = userDao.findByEmail(email);

		if (user != null && !user.isDeleted()) {
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getUserPermissions(user));
		} else {
			throw new UsernameNotFoundException(String.format("There is not enabled user id db with the following email: %s", email));
		}
	}

	/**
	 * @see com.pabloam.microservices.converter.user.services.UserServices#register(com.pabloam.microservices.converter.user.model.User)
	 */
	@Override
	public User register(User user, List<String> defaultUserGroups) {
		try {
			verifyUser(user);
			user.setCreated(Instant.now(Clock.systemUTC()).toEpochMilli());
			user.setDeleted(false);

			if (!CollectionUtils.isEmpty(defaultUserGroups)) {
				List<Group> defaultGroups = this.groupDao.findMultipleByDescriptionList(defaultUserGroups);
				user.setGroups(new HashSet<>(defaultGroups));
			}

			return userDao.save(user);

		} catch (Exception e) {
			logger.error("Exception registering user with the following values: '{}'", user, e);
			throw new UserServicesException(e.getMessage());
		}
	}

	/**
	 * @param user
	 */
	private void verifyUser(User user) {
		if (user == null) {
			throw new RegistrationException("The user to insert cannot be null.");
		}

		// Email validator according to the RFC 822 specification
		if (!EmailValidator.getInstance().isValid(user.getEmail())) {
			throw new RegistrationException("The email is not valid.");
		}

		if (!StringUtils.hasText(user.getPassword())) {
			throw new RegistrationException("The password cannot be null.");
		}

		if (!StringUtils.hasText(user.getAddress())) {
			throw new RegistrationException("The address cannot be empty.");
		}

		if (!StringUtils.hasText(user.getZipCode())) {
			throw new RegistrationException("The zipCode cannot be empty.");
		}

		if (!StringUtils.hasText(user.getCountry())) {
			throw new RegistrationException("The country cannot be empty.");
		}

		if (!isReasonableBirthDate(user.getBirthDate())) {
			throw new RegistrationException("The birthdate introduced is not reasonable.");
		}

	}

	/**
	 * @param birthDate
	 */
	private boolean isReasonableBirthDate(Long birthDate) {
		if (birthDate != null) {
			LocalDateTime userDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(birthDate), TimeZone.getDefault().toZoneId());
			long years = userDate.until(LocalDateTime.ofInstant(Instant.now(), TimeZone.getDefault().toZoneId()), ChronoUnit.YEARS);
			return (years > 10 && years < 100);
		}
		return false;

	}

	/**
	 * Gets a list of the permissions of all the user group
	 * 
	 * @param user
	 * @return
	 */
	private List<GrantedAuthority> getUserPermissions(User user) {

		Set<GrantedAuthority> authorities = new HashSet<>();

		if (!CollectionUtils.isEmpty(user.getGroups())) {
			authorities = user.getGroups().stream().map(group -> group.getPermissions()).flatMap(permission -> permission.stream())
					.map(p -> new SimpleGrantedAuthority(p.getDescription())).collect(Collectors.toSet());

		}
		return new ArrayList<GrantedAuthority>(authorities);
	}

}
