package com.pabloam.microservices.converter.user.services.web;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.user.model.User;
import com.pabloam.microservices.converter.user.services.UserServices;

@RestController
public class UserController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserServices userServices;

	@Value("#{'${default.user.groups}'.split(',')}")
	private List<String> defaultUserGroups;

	/**
	 * Returns the principal
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}

	/**
	 * Returns the user email if the user is properly registered
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map.Entry<String, String> register(@RequestBody User data) {

		Map.Entry<String, String> result = null;
		try {
			User user = userServices.register(data, defaultUserGroups);
			result = new AbstractMap.SimpleEntry<String, String>("success", user.getEmail());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new AbstractMap.SimpleEntry<String, String>("success", e.getMessage());
		}
		return result;
	}

	/**
	 * Sets the data into a User pojo
	 * 
	 * @param data
	 * @return
	 */
	private User processData(Map<String, Object> data) {
		User user = new User();
		user.setEmail((String) data.get("email"));
		user.setPassword((String) data.get("password"));

		LocalDate date = (LocalDate) data.get("birthdate");
		if (date != null) {
			user.setBirthDate(date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
		}
		user.setAddress((String) data.get("address"));
		user.setZipCode((String) data.get("zipCode"));
		return user;
	}

}
