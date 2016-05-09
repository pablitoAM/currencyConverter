package com.pabloam.microservices.converter.user.services.web;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.user.model.User;
import com.pabloam.microservices.converter.user.services.UserServices;

@RestController
public class UserRestController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(UserRestController.class);

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
	public Map<String, Object> user(Authentication user) {
		Map<String, Object> result = new HashMap<>(2);
		result.put("email", user.getName());
		result.put("authorities", user.getAuthorities().stream().map(a -> a.getAuthority().toString()).collect(Collectors.toList()));
		return result;
	}

	/**
	 * Returns the user email if the user is properly registered
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map<String, String> register(@RequestBody Map<String, Object> data) {

		Map<String, String> result = new HashMap<String, String>();
		User user = userServices.register(processData(data), defaultUserGroups);
		result.put("success", user.getEmail());
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

		String birthDateString = (String) data.get("birthDate");

		if (StringUtils.hasText(birthDateString)) {
			LocalDate date = LocalDate.parse(birthDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			user.setBirthDate(date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
		}

		user.setAddress((String) data.get("address"));
		user.setZipCode((String) data.get("zipCode"));
		user.setCountry((String) data.get("country"));

		return user;
	}

	// Error Handling
	@ExceptionHandler(Exception.class)
	public @ResponseBody Map<String, String> handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		return Collections.singletonMap("error",
				String.format("'%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage()));
	}

}
