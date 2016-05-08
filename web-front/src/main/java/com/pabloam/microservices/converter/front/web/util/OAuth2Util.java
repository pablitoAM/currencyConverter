package com.pabloam.microservices.converter.front.web.util;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Util {

	@Value("${security.oauth2.client.accessTokenUri:http://localhost:9999/auth/oauth/token}")
	private String tokenUrl;

	@Value("${security.oauth2.client.clientId:}")
	private String cliendId;

	@Value("${security.oauth2.client.clientSecret:}")
	private String clientSecret;

	@Autowired
	private OAuth2ClientContext clientContext;

	/**
	 * Gets an OAuth2RestTemplate for the given user credentials
	 * 
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
		resource.setScope(Collections.singletonList("write"));

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
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId(cliendId);
		resource.setClientSecret(clientSecret);
		resource.setGrantType("password");
		resource.setScope(Arrays.asList("read", "write"));
//		resource.setClientAuthenticationScheme(AuthenticationScheme.form);

		resource.setUsername(email);
		resource.setPassword(password);

		return resource;
	}

	public OAuth2RestTemplate getOAuth2RestTemplateForClientCredentials() {
		// TODO Auto-generated method stub
		return null;
	}
}
