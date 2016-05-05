package com.pabloam.microservices.converter.front.web.util;

import java.util.ArrayList;
import java.util.List;

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

	@Value("${security.oauth2.client.accessTokenUri:http://localhost:9999/auth/oauth/token}")
	private String tokenUrl;

	@Value("${security.oauth2.client.clientId:zoo}")
	private String cliendId;

	@Value("${security.oauth2.client.clientSecret:zoo}")
	private String clientSecret;

	/**
	 * Gets an OAuth2RestTemplate for the given user credentials
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public OAuth2RestTemplate getOAuth2RestTemplateForPassword(String email, String password) {
		ResourceOwnerPasswordAccessTokenProvider provider = new ResourceOwnerPasswordAccessTokenProvider();
		ResourceOwnerPasswordResourceDetails resource = getResource(email, password);

		OAuth2AccessToken accessToken = provider.obtainAccessToken(resource, new DefaultAccessTokenRequest());
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(accessToken));

		return restTemplate;
	}

	/**
	 * Gets an access token for the given client. To emulate an anonymous user
	 * 
	 * @return
	 */
	public OAuth2RestTemplate getOAuth2RestTemplateForClientCredentials() {
		ResourceOwnerPasswordAccessTokenProvider provider = new ResourceOwnerPasswordAccessTokenProvider();
		ClientCredentialsResourceDetails resource = getResource();

		OAuth2AccessToken accessToken = provider.obtainAccessToken(resource, new DefaultAccessTokenRequest());
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(accessToken));

		return restTemplate;
	}

	/**
	 * Gets a ClientCredentialsResourceDetails for the given client. To emulate
	 * an anonymous user
	 * 
	 * @return
	 */
	private ClientCredentialsResourceDetails getResource() {
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();

		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId(cliendId);
		resource.setClientSecret(clientSecret);
		resource.setGrantType("client-credentials");

		return resource;
	}

	/**
	 * Gets a ResourceOwnerPasswordResourceDetails for the given user
	 * credentials
	 * 
	 * @return
	 */
	private ResourceOwnerPasswordResourceDetails getResource(String email, String password) {
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

		List<String> scopes = new ArrayList<String>(2);
		scopes.add("write");
		scopes.add("read");
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId(cliendId);
		resource.setClientSecret(clientSecret);
		resource.setGrantType("password");
		resource.setScope(scopes);

		resource.setUsername(email);
		resource.setPassword(password);

		return resource;
	}
}
