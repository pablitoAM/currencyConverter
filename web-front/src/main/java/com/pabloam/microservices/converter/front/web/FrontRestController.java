package com.pabloam.microservices.converter.front.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.front.services.FrontServices;
import com.pabloam.microservices.converter.front.services.PublishingServices;

@RestController
public class FrontRestController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(FrontRestController.class);

	@Autowired
	private FrontServices frontServices;

	@Autowired
	private PublishingServices publishingServices;

	@Value("#{'${default.currencies}'.split(',')}")
	private List<String> currencies;

	@Value("${provider.prefix:PROVIDER-}")
	private String providerPrefix;

	@Value("${history.services.client.id:}")
	protected String historyServicesClientId;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> query(HttpSession session, @RequestBody Map<String, Object> data) {

		Map<String, Object> result;

		OAuth2AccessToken accessToken = (OAuth2AccessToken) session.getAttribute("token");
		addExpectedCurrencies(currencies, data);

		result = frontServices.query((String) session.getAttribute("email"), providerPrefix, accessToken, data);
		if (result.containsKey("success")) {
			try {
				this.publishingServices.publishInHistoryServices((String) session.getAttribute("email"), (String) data.get("provider"), accessToken,
						(Map<String, Object>) result.get("success"));
			} catch (Exception e) {
				logger.error("Error trying to persist the query in the history service.", e);
			}
		}

		return result;
	}

	@RequestMapping(value = "/getLast/{number}", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getLast(HttpSession session, @PathVariable int number) {
		OAuth2AccessToken accessToken = (OAuth2AccessToken) session.getAttribute("token");
		List<Map<String, Object>> result = this.frontServices.getLast(historyServicesClientId, (String) session.getAttribute("email"), number, accessToken);
		return result;
	}

	/**
	 * Adds the list of expected currencies without the source one
	 * 
	 * @param currencies
	 * @param data
	 */
	private void addExpectedCurrencies(List<String> currencies, Map<String, Object> data) {
		String source = (String) data.get("source");
		List<String> expected = currencies.stream().collect(Collectors.toList());
		expected.remove(source);
		data.put("expected", StringUtils.collectionToCommaDelimitedString(expected));
	}

	// Error Handling
	@ExceptionHandler(Exception.class)
	public @ResponseBody Map<String, String> handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		return Collections.singletonMap("error", String.format("'%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage()));
	}
}
