package com.pabloam.microservices.converter.front.web.util;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Util {

	@Value("${security.oauth2.client.clientId:}")
	private String cliendId;

	@Value("${security.oauth2.client.clientSecret:}")
	private String clientSecret;

	@Value("${security.oauth2.client.accessTokenUri:}")
	private String accessTokenUri;

	/**
	 * Gets an OAuth2RestTemplate for the given user credentials
	 * 
	 * @param tokenUrl
	 * @param email
	 * @param password
	 * @return
	 */
	public OAuth2RestTemplate getOAuth2RestTemplateForPassword(String email, String password) {

		ResourceOwnerPasswordAccessTokenProvider provider = new ResourceOwnerPasswordAccessTokenProvider();
		ResourceOwnerPasswordResourceDetails resource = getUserPasswordResource(email, password);

		OAuth2AccessToken accessToken = provider.obtainAccessToken(resource, new DefaultAccessTokenRequest());
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(accessToken));

		return restTemplate;
	}

	/**
	 * Gets an OAuth2RestTemplate for the given accessToken
	 * 
	 * @param tokenUrl
	 * @param accessToken
	 * @return
	 */
	public OAuth2RestTemplate getOAuth2RestTemplateForToken(OAuth2AccessToken accessToken) {

		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(getResource(), new DefaultOAuth2ClientContext(accessToken));
		return restTemplate;

	}

	/**
	 * Gets a ClientCredentialsResourceDetails for the given client.
	 * 
	 * @return
	 */
	private ClientCredentialsResourceDetails getResource() {
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();

		resource.setAccessTokenUri(accessTokenUri);
		resource.setClientId(cliendId);
		resource.setClientSecret(clientSecret);
		resource.setGrantType("client-credentials");
		resource.setScope(Arrays.asList("read", "write"));

		return resource;
	}

	/**
	 * Gets a ResourceOwnerPasswordResourceDetails for the given user
	 * credentials
	 * 
	 * @return
	 */
	private ResourceOwnerPasswordResourceDetails getUserPasswordResource(String email, String password) {
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		resource.setAccessTokenUri(accessTokenUri);
		resource.setClientId(cliendId);
		resource.setClientSecret(clientSecret);
		resource.setGrantType("password");
		resource.setScope(Arrays.asList("read", "write"));

		resource.setUsername(email);
		resource.setPassword(password);

		return resource;
	}
}
