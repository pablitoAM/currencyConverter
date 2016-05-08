package com.pabloam.microservices.converter.front.web;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pabloam.microservices.converter.front.services.FrontServices;
import com.pabloam.microservices.converter.front.web.util.UriUtil;

@Controller
public class FrontController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(FrontController.class);

	@Autowired
	private FrontServices frontServices;

	@Autowired
	private UriUtil uriUtil;

	@Value("#{'${default.currencies}'.split(',')}")
	private List<String> currencies;

	@Value("${provider.prefix:PROVIDER-}")
	private String providerPrefix;

	/**
	 * The index page
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/", "/index" })
	public String index(HttpSession session, Model model) {
		if (session.getAttribute("email") != null) {
			model.addAttribute("providers", frontServices.getActiveProviders(providerPrefix));
			model.addAttribute("currencies", currencies);
		}
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
	public String login(@RequestParam("f_un") String email, @RequestParam("f_pw") String password, HttpServletRequest request, Model model) {

		try {

			// Get the token
			OAuth2AccessToken accessToken = this.frontServices.getAccessToken(email, password);
			logger.debug("Token received: {}", accessToken);

			// Ask for user data (I could decode the token but this way I could
			// gather more info from the user than only the credentials)

			URI credentialsUril = uriUtil.composeUri(request, "/auth/user");
			Map<String, Object> userCredentials = this.frontServices.getUserCredentials(credentialsUril.toString(), accessToken);
			logger.debug("Credentials received: {}", userCredentials);

			storeInSession(request.getSession(), accessToken, userCredentials);

			logger.debug("Redirecting to index");
			return "redirect:index";

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			model.addAttribute("error", e.getMessage());
		}
		return "index";
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
		return "redirect:index";
	}

	/**
	 * Stores the user credentials in the session
	 * 
	 * @param session
	 * @param userCredentials
	 */
	private void storeInSession(HttpSession session, OAuth2AccessToken accessToken, Map<String, Object> credentials) {
		if (!CollectionUtils.isEmpty(credentials)) {
			session.setAttribute("email", credentials.get("email"));
			session.setAttribute("authorities", credentials.get("authorities"));
		}
		session.setAttribute("token", accessToken);
	}

}
