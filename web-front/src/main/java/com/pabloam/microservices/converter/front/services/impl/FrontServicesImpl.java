/**
 * 
 */
package com.pabloam.microservices.converter.front.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.pabloam.microservices.converter.common.DefaultResponse;
import com.pabloam.microservices.converter.common.ResponseStatus;
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

	@Value("${security.oauth2.client.registerUserUrl:}")
	private String registerUserUrl;

	@Value("${security.oauth2.client.clientId:}")
	private String clientId;

	@Value("${security.oauth2.client.clientSecret:}")
	private String clientSecret;

	@Value("${provider.prefix:PROVIDER-}")
	protected String providerPrefix;

	@Autowired
	private DiscoveryClient discoveryClient;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pabloam.microservices.converter.front.services.FrontServices#
	 * getAccessToken(java.lang.String, java.lang.String)
	 */
	@Override
	public OAuth2RestTemplate getOAuth2RestTemplateForUserPassword(String email, String password) throws FrontServicesException {

		try {
			validateCredentials(email, password);
			OAuth2RestTemplate oAuth2RestTemplate = oauth2Util.getOAuth2RestTemplateForPassword(email, password);
			return oAuth2RestTemplate;

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

			String base64Creds = prepareClientCredentials(this.clientId, this.clientSecret);
			HttpHeaders headers = prepareClientHeaders(base64Creds);

			OAuth2RestTemplate oAuth2RestTemplate = this.oauth2Util.getOAuth2RestTemplateForClientCredentials();
			HttpEntity<Map> entity = new HttpEntity<Map>(user, headers);
			ResponseEntity<DefaultResponse> httpResponse = oAuth2RestTemplate.exchange(registerUserUrl, HttpMethod.POST, entity, DefaultResponse.class);

			validateResponse(httpResponse);

		} catch (Exception e) {
			logger.error("Exception registering '{}'", user, e);
			throw new FrontServicesException(String.format("Exception registering user: %s", e.getMessage()));
		}
	}

	@Override
	public List<String> getActiveProviders() {
		List<String> services = this.discoveryClient.getServices();
		return services.stream().filter(s -> s.startsWith(providerPrefix)).map(s -> s.replace(providerPrefix, "")).collect(Collectors.toList());
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

	/**
	 * Throws exception if the response is 4xx or 5xx or the response status is
	 * ERROR
	 * 
	 * @param httpResponse
	 */
	private void validateResponse(ResponseEntity<DefaultResponse> httpResponse) {
		if (httpResponse.getStatusCode().is4xxClientError() || httpResponse.getStatusCode().is5xxServerError()) {
			throw new FrontServicesException(httpResponse.getStatusCode().getReasonPhrase());
		} else {
			DefaultResponse response = httpResponse.getBody();
			if (response != null && ResponseStatus.ERROR.equals(response.getStatus())) {
				throw new FrontServicesException(response.getPayload().toString());
			}
		}
	}

	/**
	 * Prepare the headers with the encoded credentials
	 * 
	 * @param base64Creds
	 * @return
	 */
	private HttpHeaders prepareClientHeaders(String base64Creds) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	/**
	 * Prepare the credentials to be encoded
	 */
	private String prepareClientCredentials(String clientId, String clientSecret) {
		byte[] base64CredsBytes = Base64.encode(String.format("%s:%s", clientId, clientSecret).getBytes());
		String base64Creds = new String(base64CredsBytes);
		return base64Creds;
	}

}
