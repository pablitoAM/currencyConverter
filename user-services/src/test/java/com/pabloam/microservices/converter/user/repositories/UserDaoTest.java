package com.pabloam.microservices.converter.user.repositories;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pabloam.microservices.converter.user.config.DatabaseConfiguration;
import com.pabloam.microservices.converter.user.model.User;

/**
 * @author Pablo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({ "jpa, h2" })
@SpringApplicationConfiguration(classes = DatabaseConfiguration.class)
public class UserDaoTest {

	@Autowired
	UserDao userDao;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.user.repositories.UserDao#findByEmail(java.lang.String)}
	 * .
	 */
	@Test
	public void testFindByEmail() throws Exception {
		/*
		 * We create a user and try to find it in the database
		 */
		String email = "test@test.com";
		User expected = createUser(email);

		userDao.save(expected);

		User actual = userDao.findByEmail(email);

		assertEquals(expected, actual);
	}

	/**
	 * @param email
	 * @return
	 */
	private User createUser(String email) {

		User user = new User();
		user.setEmail(email);
		user.setPassword("foo");
		user.setAddress("bar 42");
		user.setCountry("Panama");
		user.setZipCode("12345GH");
		user.setBirthDate(Instant.parse("1984-12-03T10:15:30.00Z").toEpochMilli());

		user.setCreated(Instant.now().toEpochMilli());
		user.setDeleted(false);

		return user;

	}

}
