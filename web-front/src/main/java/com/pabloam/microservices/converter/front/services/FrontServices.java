package com.pabloam.microservices.converter.front.services;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.pabloam.microservices.converter.front.exceptions.FrontServicesException;

public interface FrontServices {

	/**
	 * Tries to get the access token for the current crendentials
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws FrontServicesException
	 */
	public OAuth2AccessToken getAccessToken(String email, String password) throws FrontServicesException;

	/**
	 * Returns the list of provider-services registered in the app with the
	 * given prefix
	 * 
	 * @param prefix
	 * @return
	 */
	public List<String> getActiveProviders(String prefix);

	/**
	 * Tries to get the userCredentials for the given accessToken
	 * 
	 * @param url
	 * @param accessToken
	 * @return
	 * @throws FrontServicesException
	 */
	public Map<String, Object> getUserCredentials(String url, OAuth2AccessToken accessToken) throws FrontServicesException;

	/**
	 * Validates the input data and queries the provider associated to that data
	 * with the given accessToken
	 * 
	 * @param providerPrefix
	 * @param accessToken
	 * @param data
	 * @return
	 * @throws FrontServicesException
	 */
	public Map<String, Object> query(String email, String providerPrefix, OAuth2AccessToken accessToken, Map<String, Object> data)
			throws FrontServicesException;

	/**
	 * Gets the last number queries from the user
	 *
	 * 
	 * @param user
	 * @param number
	 * @param accessToken
	 * @return
	 */
	public List<Map<String, Object>> getLast(String serviceId, String user, int number, OAuth2AccessToken accessToken);

}
