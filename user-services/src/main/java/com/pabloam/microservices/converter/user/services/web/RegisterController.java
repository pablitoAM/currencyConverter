package com.pabloam.microservices.converter.user.services.web;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pabloam.microservices.converter.user.model.User;
import com.pabloam.microservices.converter.user.services.UserServices;

@Controller
public class RegisterController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(RegisterController.class);

	@Value("#{'${default.user.groups}'.split(',')}")
	private List<String> defaultUserGroups;

	@Autowired
	private UserServices userServices;

	/**
	 * Returns the user email if the user is properly registered
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestBody Map<String, Object> data) {

		Map.Entry<String, String> result = null;
		try {
			User user = userServices.register(processData(data), defaultUserGroups);
			result = new AbstractMap.SimpleEntry<String, String>("success", user.getEmail());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new AbstractMap.SimpleEntry<String, String>("error", e.getMessage());
		}
		return result.getValue();
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

	// Error Handling
	@ExceptionHandler(Exception.class)
	public ModelAndView handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		ModelAndView mv = new ModelAndView("/");
		mv.addObject("error", String.format("Exception in request: '%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage()));
		return mv;
	}
}
