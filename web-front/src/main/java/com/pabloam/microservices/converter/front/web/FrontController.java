package com.pabloam.microservices.converter.front.web;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.pabloam.microservices.converter.front.services.FrontServices;

@Controller
public class FrontController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(FrontController.class);

	@Autowired
	private FrontServices frontServices;

	@Value("${security.oauth2.resource.userInfoUri}")
	private String authUserUri;

	@RequestMapping({ "/", "/index" })
	public String index(Authentication auth, Model model, @RequestParam(value = "error", required = false) String error) {
		if (auth != null && !(auth instanceof PreAuthenticatedAuthenticationToken)) {
			model.addAttribute("user", auth.getName());
		}

		if (StringUtils.hasText(error)) {
			model.addAttribute("error");
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("f_un") String email, @RequestParam("f_pw") String password, HttpServletRequest request, Model model) {

		String result = "redirect:index";

		try {

			Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
			if (currentAuth != null && CollectionUtils.isEmpty(currentAuth.getAuthorities())) {
				return result;
			} else {
				Authentication preAuth = new UsernamePasswordAuthenticationToken(email, password);
				SecurityContextHolder.getContext().setAuthentication(preAuth);
			}

			OAuth2RestTemplate restTemplate = this.frontServices.getOAuth2RestTemplateForUserPassword(email, password);
			OAuth2AccessToken accessToken = restTemplate.getAccessToken();

			URI uri = UriComponentsBuilder.newInstance().scheme(request.getScheme()).host(request.getServerName()).port(request.getServerPort())
					.path("/auth/user").build().toUri();
			Map<String, Object> userCredentials = (Map<String, Object>) restTemplate.getForObject(uri, Map.class);
			storeCredentialsInSecurityContext(accessToken, userCredentials);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = String.format("%s?error=%s", result, e.getMessage());
		}
		return result;
	}

	private void storeCredentialsInSecurityContext(OAuth2AccessToken accessToken, Map<String, Object> userCredentials) {
		Authentication loggedAuth = new UsernamePasswordAuthenticationToken(userCredentials.get("email"), accessToken,
				(Collection<? extends GrantedAuthority>) userCredentials.get("authorities"));
		SecurityContextHolder.getContext().setAuthentication(loggedAuth);
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

	// @RequestMapping(value = "/register", method = RequestMethod.POST)
	// public ModelAndView register(@RequestParam Map<String, String> allParams,
	// Model model, HttpSession session) {
	//
	// ModelAndView mv = new ModelAndView("index");
	// try {
	//
	// this.frontServices.register(allParams);
	//
	// String email = allParams.get("email");
	// String password = allParams.get("password");
	//
	// OAuth2AccessToken accessToken =
	// this.frontServices.getOAuth2RestTemplateForUserPassword(email, password);
	//
	// } catch (Exception e) {
	// // Do nothing
	// mv.addObject("error", e.getMessage());
	// session.invalidate();
	// }
	// return mv;
	// }

}
