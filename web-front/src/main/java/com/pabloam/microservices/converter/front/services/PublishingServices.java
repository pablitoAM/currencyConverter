package com.pabloam.microservices.converter.front.services;

import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.pabloam.microservices.converter.front.exceptions.PublishingException;

public interface PublishingServices {

	/**
	 * Publishes the response in history services
	 * 
	 * @param user
	 * @param provider
	 * @param accessToken
	 * @param response
	 * @throws PublishingException
	 */
	public void publishInHistoryServices(String user, String provider, OAuth2AccessToken accessToken, Map<String, Object> response) throws PublishingException;

}
