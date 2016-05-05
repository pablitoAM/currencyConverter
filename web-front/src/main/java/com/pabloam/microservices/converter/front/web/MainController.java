package com.pabloam.microservices.converter.front.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pabloam.microservices.converter.front.services.FrontServices;

@Controller
public class MainController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(MainController.class);

	@Autowired
	private FrontServices frontServices;

	/**
	 * Processes the normal requests
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "/", "/index", "/login" })
	public String index() {
		return "index";
	}

	/**
	 * Processes the login process trying to get an access token and storing it
	 * in session
	 * 
	 * @param credentials
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam("f_un") String email, @RequestParam("f_pw") String password, HttpSession session) {

		ModelAndView mv = new ModelAndView("index");
		try {
			OAuth2AccessToken accessToken = this.frontServices.getAccessToken(email, password);
			storeInSession(email, accessToken, session);
		} catch (Exception e) {
			// Do nothing
			mv.addObject("error", e.getMessage());
			session.invalidate();
		}
		return mv;
	}

	/**
	 * Manages the login out process
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "index";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(@RequestParam Map<String, String> allParams, Model model, HttpSession session) {

		ModelAndView mv = new ModelAndView("index");
		try {

			this.frontServices.register(allParams);

			String email = allParams.get("email");
			String password = allParams.get("password");

			OAuth2AccessToken accessToken = this.frontServices.getAccessToken(email, password);
			storeInSession(email, accessToken, session);

		} catch (Exception e) {
			// Do nothing
			mv.addObject("error", e.getMessage());
			session.invalidate();
		}
		return mv;
	}

	/**
	 * Stores in session the following elements
	 * 
	 * @param email
	 * @param session
	 * @param accessToken
	 */
	private void storeInSession(String email, OAuth2AccessToken accessToken, HttpSession session) {
		session.setAttribute("user", email);
		session.setAttribute("token", accessToken);
	}

	// Error Handling
	@ExceptionHandler(Exception.class)
	public ModelAndView handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("error", String.format("Exception in request: '%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage()));
		return mv;
	}
}
