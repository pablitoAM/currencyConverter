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
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient.EurekaServiceInstance;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
	private DiscoveryClient discoveryClient;

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
		} catch (Exception e) {
			logger.error("Error getting the access token for: '{}:{}'", email, password, e);
			throw new FrontServicesException(String.format("Error getting the access token %s", e.getMessage()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pabloam.microservices.converter.front.services.FrontServices#
	 * getUserCredentials(java.lang.String,
	 * org.springframework.security.oauth2.common.OAuth2AccessToken)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getUserCredentials(String url, OAuth2AccessToken accessToken) throws FrontServicesException {
		try {
			OAuth2RestTemplate restTemplate = oauth2Util.getOAuth2RestTemplateForToken(accessToken);
			return restTemplate.getForObject(url, Map.class);
		} catch (Exception e) {
			logger.error("Error getting the user credentials for the url {} and accessToken {}", url, accessToken, e);
			throw new FrontServicesException(String.format("Error getting the user credentials: %s", e.getMessage()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.front.services.FrontServices#getLast(
	 * java.lang.String, int,
	 * org.springframework.security.oauth2.common.OAuth2AccessToken)
	 */
	@Override
	public Map<String, Object> getLast(String serviceId, String user, int number, OAuth2AccessToken accessToken) {
		try {
			String url = prepareUrlForGetLast(serviceId, user, number);
			OAuth2RestTemplate restTemplate = oauth2Util.getOAuth2RestTemplateForToken(accessToken);
			return restTemplate.getForObject(url, Map.class);
		} catch (Exception e) {
			logger.error("Error getting the last {} queries for the user {}", number, user, e);
			throw new FrontServicesException(String.format("Error getting the user credentials: %s", e.getMessage()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pabloam.microservices.converter.front.services.FrontServices#
	 * getActiveProviders(java.lang.String)
	 */
	@Override
	public List<String> getActiveProviders(String prefix) {
		List<String> services = this.discoveryClient.getServices();
		return services.stream().filter(s -> s.toUpperCase().startsWith(prefix.toUpperCase())).map(s -> s.toUpperCase().replace(prefix.toUpperCase(), ""))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.front.services.FrontServices#query(
	 * java.lang.String,
	 * org.springframework.security.oauth2.common.OAuth2AccessToken,
	 * java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String email, String prefix, OAuth2AccessToken accessToken, Map<String, Object> data) throws FrontServicesException {

		try {
			verifyData(data);
			String url = prepareUrlForProvider(email, prefix, data);

			OAuth2RestTemplate restTemplate = oauth2Util.getOAuth2RestTemplateForToken(accessToken);
			return restTemplate.getForObject(url, Map.class);

		} catch (Exception e) {
			logger.error("Error in query with the given data: {}", data, e);
			throw new FrontServicesException(String.format("Error in query: %s", e.getMessage()));
		}
	}

	/**
	 * We have to prepare the url for the provider according to the information
	 * stored in data
	 * 
	 * @param prefix
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String prepareUrlForProvider(String email, String prefix, Map<String, Object> data) {

		String result = null;

		String provider = (String) data.get("provider");
		String source = (String) data.get("source");
		boolean historical = (boolean) data.get("historical");
		String date = (String) data.get("date");
		String expected = (String) data.get("expected");

		List<ServiceInstance> instances = this.discoveryClient.getInstances(String.format("%s%s", prefix, provider));
		if (!CollectionUtils.isEmpty(instances)) {
			EurekaServiceInstance firstInstance = (EurekaServiceInstance) instances.get(0);

			if (historical) {
				// provider/getHistorical/{date}/{sourceCurrency}/{expectedCurrencies}
				result = String.format("%s/provider/getHistorical/%s/%s/%s/%s", firstInstance.getUri(), email, date, source, expected);
			} else {
				// provider/getCurrent/{sourceCurrency}/{expectedCurrencies}
				result = String.format("%s/provider/getCurrent/%s/%s/%s", firstInstance.getUri(), email, source, expected);
			}
		}
		return result;

	}

	private String prepareUrlForGetLast(String serviceId, String user, int number) {

		String result = null;
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
		if (!CollectionUtils.isEmpty(instances)) {
			ServiceInstance serviceInstance = instances.get(0);
			result = String.format("%s/history/getLast?n=%s&u=%s", serviceInstance.getUri(), number, user);
		}
		return result;

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
	 * @param data
	 */
	private void verifyData(Map<String, Object> data) {

		if (CollectionUtils.isEmpty(data)) {
			throw new IllegalArgumentException("The data cannot be empty.");
		} else {

			String provider = (String) data.get("provider");
			if (!StringUtils.hasText(provider)) {
				throw new IllegalArgumentException("The provider cannot be empty.");
			}

			String source = (String) data.get("source");
			if (!StringUtils.hasText(source)) {
				throw new IllegalArgumentException("The source source be empty.");
			}

			boolean historical = (boolean) data.get("historical");
			if (historical) {
				String date = (String) data.get("date");
				if (!StringUtils.hasText(date)) {
					throw new IllegalArgumentException("The date cannot be empty in a historical query");
				}
			}

			String expected = (String) data.get("expected");
			if (!StringUtils.hasText(expected)) {
				throw new IllegalArgumentException("The expected currencies cannot be empty.");
			}

		}
	}

}
