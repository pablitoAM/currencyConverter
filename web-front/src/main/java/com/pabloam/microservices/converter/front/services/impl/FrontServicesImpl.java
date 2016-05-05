/**
 * 
 */
package com.pabloam.microservices.converter.front.services.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.pabloam.microservices.converter.front.exceptions.BadCredentialsException;
import com.pabloam.microservices.converter.front.exceptions.FrontServicesException;
import com.pabloam.microservices.converter.front.services.FrontServices;
import com.pabloam.microservices.converter.front.web.util.OAuth2Util;

/**
 * @author PabloAM
 *
 */
@Service
public class FrontServicesImpl implements FrontServices {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(FrontServicesImpl.class);

	@Autowired
	private OAuth2Util oauth2Util;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${registerUserUrl:}")
	private String registerUserUrl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pabloam.microservices.converter.front.services.FrontServices#
	 * getAccessToken(java.lang.String, java.lang.String)
	 */
	@Override
	public OAuth2AccessToken getAccessToken(String email, String password) throws FrontServicesException {

		try {
			validateCredentials(email, password);
			OAuth2RestTemplate oAuth2RestTemplate = oauth2Util.getOAuth2RestTemplateForPassword(email, password);
			return oAuth2RestTemplate.getAccessToken();

		} catch (IllegalArgumentException | OAuth2AccessDeniedException e) {
			logger.error("Exception getting the access token for: '{}:{}'", email, password, e);
			throw new BadCredentialsException(String.format("Exception getting the access token %s", e.getMessage()));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.front.services.FrontServices#register
	 * (java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void register(Map<String, String> user) throws FrontServicesException {

		if (CollectionUtils.isEmpty(user)) {
			throw new IllegalArgumentException("The user to register cannot be empty.");
		}
		try {

			OAuth2RestTemplate oAuth2RestTemplate = this.oauth2Util.getOAuth2RestTemplateForClientCredentials();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Map<String, String>> entity = new HttpEntity<Map<String, String>>(user, headers);
			ResponseEntity<Map> response = oAuth2RestTemplate.postForEntity(registerUserUrl, entity, Map.class);

			if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
				throw new FrontServicesException(response.getStatusCode().getReasonPhrase());
			}

		} catch (Exception e) {
			logger.error("Exception registering '%s'", user, e);
			throw new FrontServicesException(String.format("Exception registering user: %s", e.getMessage()));
		}
	}

	/**
	 * Validates if the credentials are not null nor empty
	 * 
	 * @param credentials
	 */
	private void validateCredentials(String email, String password) {
		if (!StringUtils.hasText(email)) {
			throw new IllegalArgumentException("The email cannot be null nor empty");
		}

		if (!StringUtils.hasText(password)) {
			throw new IllegalArgumentException("The password cannot be null nor empty");
		}
	}
}
