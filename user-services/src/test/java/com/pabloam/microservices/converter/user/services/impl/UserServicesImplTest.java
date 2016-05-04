package com.pabloam.microservices.converter.user.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pabloam.microservices.converter.user.exception.UserServicesException;
import com.pabloam.microservices.converter.user.model.Group;
import com.pabloam.microservices.converter.user.model.Permission;
import com.pabloam.microservices.converter.user.model.User;
import com.pabloam.microservices.converter.user.repositories.UserDao;

/**
 * @author Pablo
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServicesImplTest {

	@Mock
	private UserDao userDao;

	@InjectMocks
	private UserServicesImpl userServicesImpl;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#loadUserByUsername(java.lang.String)}
	 * .
	 */
	@Test
	public void testLoadUserByUsername() throws Exception {
		/*
		 * We try to find the user by the username, in our case is the same as
		 * the user email. We verify that the email is not null nor empty and
		 * that the user exists in the database and is not deleted. We verify
		 * that the returning userDetails has only one instance of each
		 * permission
		 */

		String email = "existingUser";
		String pass = "pass";

		User user = mock(User.class);

		Group group1 = mock(Group.class);
		Group group2 = mock(Group.class);

		Set<Group> userGroups = new HashSet<Group>();
		userGroups.add(group1);
		userGroups.add(group2);

		Permission permission1 = mock(Permission.class);
		Permission permission2 = mock(Permission.class);
		Set<Permission> groupPermissions = new HashSet<Permission>();
		groupPermissions.add(permission1);
		groupPermissions.add(permission2);

		doReturn(user).when(userDao).findByEmail(email);
		doReturn(false).when(user).isDeleted();
		doReturn(email).when(user).getEmail();
		doReturn(pass).when(user).getPassword();

		doReturn(userGroups).when(user).getGroups();
		doReturn(groupPermissions).when(group1).getPermissions();
		doReturn(groupPermissions).when(group2).getPermissions();

		doReturn("Perm1").when(permission1).getDescription();
		doReturn("Perm2").when(permission2).getDescription();

		List<String> expectedPermissions = new ArrayList<String>(2);
		expectedPermissions.add("Perm1");
		expectedPermissions.add("Perm2");

		UserDetails userDetails = this.userServicesImpl.loadUserByUsername(email);

		assertEquals(email, userDetails.getUsername());
		assertEquals(pass, userDetails.getPassword());

		assertEquals(groupPermissions.size(), userDetails.getAuthorities().size());
		userDetails.getAuthorities().stream().forEach(auth -> assertTrue(expectedPermissions.contains(auth.getAuthority())));

		verify(this.userDao).findByEmail(email);
		verifyNoMoreInteractions(this.userDao);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#loadUserByUsername(java.lang.String)}
	 * .
	 */
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameDeletedUser() throws Exception {
		/*
		 * Throws UsernameNotFoundException if the user is deleted
		 */
		String email = "deletedUser";
		User user = mock(User.class);

		doReturn(user).when(userDao).findByEmail(email);
		doReturn(true).when(user).isDeleted();

		this.userServicesImpl.loadUserByUsername(email);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#loadUserByUsername(java.lang.String)}
	 * .
	 */
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameUserNotFound() throws Exception {
		/*
		 * Throws UsernameNotFoundException if the user is not in db
		 */
		String email = "notFoundUser";

		doReturn(null).when(userDao).findByEmail(email);

		this.userServicesImpl.loadUserByUsername(email);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#loadUserByUsername(java.lang.String)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadUserByUsernameWrongEmail() throws Exception {
		/*
		 * Throws exception when the user email is null or empty
		 */
		this.userServicesImpl.loadUserByUsername(null);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#register(com.pabloam.microservices.converter.user.model.User)}
	 * .
	 */
	@Test
	public void testRegister() throws Exception {
		/*
		 * Validate the user received parameters and verify the method save is
		 * invoked in the dao
		 */
		String email = "test@test.com";
		User user = getUser(email);
		doReturn(user).when(this.userDao).save(user);

		this.userServicesImpl.register(user);
		verify(this.userDao).save(user);
		verifyNoMoreInteractions(this.userDao);

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#register(com.pabloam.microservices.converter.user.model.User)}
	 * .
	 */
	@Test(expected = UserServicesException.class)
	public void testRegisterUnreasonableDateTooYoung() throws Exception {
		/*
		 * Throws exception if the birthdate is not reasonable
		 */
		String email = "test@test.com";
		User user = getUser(email);

		Long fromTheFutureDate = Instant.parse("2864-12-03T10:15:30.00Z").toEpochMilli();
		user.setBirthDate(fromTheFutureDate);

		this.userServicesImpl.register(user);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#register(com.pabloam.microservices.converter.user.model.User)}
	 * .
	 */
	@Test(expected = UserServicesException.class)
	public void testRegisterUnReasonableDateTooOld() throws Exception {
		/*
		 * Throws exception if the birthdate is not reasonable
		 */
		String email = "test@test.com";
		User user = getUser(email);

		Long tooOldDate = Instant.parse("1864-12-03T10:15:30.00Z").toEpochMilli();
		user.setBirthDate(tooOldDate);

		this.userServicesImpl.register(user);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#register(com.pabloam.microservices.converter.user.model.User)}
	 * .
	 */
	@Test(expected = UserServicesException.class)
	public void testRegisterEmptyPassword() throws Exception {
		/*
		 * Throws exception if the password is empty
		 */
		String email = "test@test.com";
		User user = getUser(email);
		user.setPassword("");

		this.userServicesImpl.register(user);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.services.impl.UserServicesImpl#register(com.pabloam.microservices.converter.user.model.User)}
	 * .
	 */
	@Test(expected = UserServicesException.class)
	public void testRegisterInvalidEmail() throws Exception {
		/*
		 * Throws exception if the email is invalid
		 */
		String invalidMail = "user@localhost";
		this.userServicesImpl.register(getUser(invalidMail));

	}

	/**
	 * @param email
	 * @return
	 */
	private User getUser(String email) {
		User user = new User();
		user.setEmail(email);
		user.setPassword("pass");
		user.setAddress("address");
		user.setCountry("Seychelles");
		user.setZipCode("1234z");

		Long birthDate = Instant.parse("1964-12-03T10:15:30.00Z").toEpochMilli();
		user.setBirthDate(birthDate);

		return user;
	}

}
