package com.pabloam.microservices.converter.user.services.web;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.user.model.User;

@RestController
public class UserRestController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(UserRestController.class);

	// @Autowired
	// private UserServices userServices;

	// @Value("#{'${default.user.groups}'.split(',')}")
	// private List<String> defaultUserGroups;

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
	// @RequestMapping(value = "/register", method = RequestMethod.POST)
	// public DefaultResponse register(@RequestBody Map<String, Object> data) {
	//
	// DefaultResponse result = new DefaultResponse();
	// try {
	// User user = userServices.register(processData(data), defaultUserGroups);
	// result =
	// result.setStatus(ResponseStatus.SUCCESS).setPayload(user.getEmail());
	//
	// } catch (Exception e) {
	// logger.error(e.getMessage(), e);
	// result =
	// result.setStatus(ResponseStatus.ERROR).setPayload(e.getMessage());
	// }
	// return result;
	// }

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
	public @ResponseBody String handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		return String.format("Exception in request: '%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage());
	}

}
