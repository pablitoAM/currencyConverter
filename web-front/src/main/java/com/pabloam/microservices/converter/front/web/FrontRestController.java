package com.pabloam.microservices.converter.front.web;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.front.services.FrontServices;
import com.pabloam.microservices.converter.front.web.util.UriUtil;

@RestController
public class FrontRestController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(FrontRestController.class);

	@Autowired
	private FrontServices frontServices;

	@Autowired
	private UriUtil uriUtil;

	@Value("#{'${default.currencies}'.split(',')}")
	private List<String> currencies;

	@Value("${provider.prefix:PROVIDER-}")
	private String providerPrefix;

	@RequestMapping("/query")
	public @ResponseBody Map<String, Object> query(HttpServletRequest request, HttpSession session, @RequestBody Map<String, Object> data) {

		Map<String, Object> result;

		try {
			OAuth2AccessToken accessToken = (OAuth2AccessToken) session.getAttribute("token");
			URI providerUri = uriUtil.composeUri(request, String.format("/%s%s", providerPrefix, data.get("provider")));

			addExpectedCurrencies(currencies, data);

			result = frontServices.query(providerUri.toString(), accessToken, data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = Collections.singletonMap("error", e.getMessage());
		}
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
		currencies.remove(source);
		data.put("expected", StringUtils.collectionToCommaDelimitedString(currencies));
	}

}
