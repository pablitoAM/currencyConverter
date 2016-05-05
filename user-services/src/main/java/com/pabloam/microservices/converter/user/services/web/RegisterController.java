package com.pabloam.microservices.converter.user.services.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

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
	// @RequestMapping(value = "/register", method = RequestMethod.POST)
	// public @ResponseBody Map.Entry<String, String> register(@RequestBody
	// Map<String, Object> data) {
	//
	// Map.Entry<String, String> result = null;
	// try {
	// User user = userServices.register(data, defaultUserGroups);
	// result = new AbstractMap.SimpleEntry<String, String>("success",
	// user.getEmail());
	//
	// } catch (Exception e) {
	// logger.error(e.getMessage(), e);
	// result = new AbstractMap.SimpleEntry<String, String>("success",
	// e.getMessage());
	// }
	// return result;
	// }

	// Error Handling
	@ExceptionHandler(Exception.class)
	public ModelAndView handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		ModelAndView mv = new ModelAndView("/");
		mv.addObject("error", String.format("Exception in request: '%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage()));
		return mv;
	}
}
