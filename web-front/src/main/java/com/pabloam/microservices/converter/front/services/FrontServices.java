package com.pabloam.microservices.converter.front.services;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.pabloam.microservices.converter.front.exceptions.FrontServicesException;

public interface FrontServices {

	/**
	 * Tries to get the restTemplate object for the current user credentials.
	 * Throws exception if the credentials are wrong or if it cannot get
	 * restTemplate.
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws FrontServicesException
	 */
	public OAuth2RestTemplate getOAuth2RestTemplateForUserPassword(String email, String password) throws FrontServicesException;

	/**
	 * Tries to register the user against the user-services. If error throws
	 * exception
	 * 
	 * @param data
	 * @return
	 * @throws FrontServicesException
	 */
	public void register(Map<String, String> user) throws FrontServicesException;

	/**
	 * Returns the list of provider-services registered in the app
	 * 
	 * @return
	 */
	public List<String> getActiveProviders();

}
