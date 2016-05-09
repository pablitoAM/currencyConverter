/**
 * 
 */
package com.pabloam.microservices.converter.front.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.pabloam.microservices.converter.front.exceptions.FrontServicesException;
import com.pabloam.microservices.converter.front.web.util.OAuth2Util;

/**
 * @author PabloAM
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontServicesImplTest {

	@Mock
	private OAuth2Util oauth2Util;

	@Mock
	private DiscoveryClient discoveryClient;

	@InjectMocks
	private FrontServicesImpl frontServicesImpl;

	@Mock
	private OAuth2RestTemplate restTemplate;

	@Mock
	private OAuth2AccessToken accessToken;

	private String email = "test@test.com";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getAccessToken(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAccessToken() throws Exception {
		/*
		 * Verify the credentials are not null nor empty and validate oauthUtil
		 * is invoked.
		 */
		String email = "asdw";
		String password = "test";

		doReturn(restTemplate).when(this.oauth2Util).getOAuth2RestTemplateForPassword(email, password);
		doReturn(accessToken).when(restTemplate).getAccessToken();

		this.frontServicesImpl.getAccessToken(email, password);
		verify(this.oauth2Util).getOAuth2RestTemplateForPassword(email, password);
		verifyNoMoreInteractions(this.oauth2Util);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getOAuth2RestTemplateForUserPassword(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
	public void testGetAccessTokenWrongPassword() throws Exception {
		/*
		 * Throws exception when the password is null or empty
		 */
		this.frontServicesImpl.getAccessToken("emai", null);

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getOAuth2RestTemplateForUserPassword(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
	public void testGetAccessTokenWrongEmail() throws Exception {
		/*
		 * Throws exception when the email is null or empty
		 */
		this.frontServicesImpl.getAccessToken(null, "password");
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getActiveProviders()}
	 * .
	 */
	@Test
	public void testGetActiveProviders() throws Exception {
		/*
		 * Retrieve the list of services registered in eureka starting with the
		 * providerPrefix
		 */
		List<String> services = getServicesList();
		services.add("p1");
		services.add("p2");

		doReturn(services).when(this.discoveryClient).getServices();
		List<String> activeProviders = this.frontServicesImpl.getActiveProviders("p");

		verify(this.discoveryClient).getServices();
		verifyNoMoreInteractions(this.discoveryClient);

		assertEquals(2, activeProviders.size());
		assertTrue(activeProviders.contains("1"));
		assertTrue(activeProviders.contains("2"));

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getUserCredentials(java.lang.String, org.springframework.security.oauth2.common.OAuth2AccessToken)}
	 * .
	 */
	@Test
	public void testGetUserCredentials() throws Exception {
		/*
		 * Verifies that a token is got and the restTemplate invoked
		 */
		doReturn(restTemplate).when(this.oauth2Util).getOAuth2RestTemplateForToken(accessToken);
		doReturn(null).when(this.restTemplate).getForObject("test", Map.class);
		this.frontServicesImpl.getUserCredentials("test", accessToken);

		verify(this.oauth2Util).getOAuth2RestTemplateForToken(accessToken);
		verify(this.restTemplate).getForObject("test", Map.class);
		verifyNoMoreInteractions(this.restTemplate, this.oauth2Util);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getActiveProviders()}
	 * .
	 */
	@Test
	public void testGetActiveProvidersNone() throws Exception {
		/*
		 * Verifies what happens if the list is empty
		 * 
		 */
		List<String> services = getServicesList();

		doReturn(services).when(this.discoveryClient).getServices();
		List<String> activeProviders = this.frontServicesImpl.getActiveProviders("p");

		verify(this.discoveryClient).getServices();
		verifyNoMoreInteractions(this.discoveryClient);

		assertEquals(0, activeProviders.size());
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#query(java.lang.String, org.springframework.security.oauth2.common.OAuth2AccessToken, java.util.Map)}
	 * .
	 */
	@Test
	public void testQuery() throws Exception {
		/*
		 * Verifies that the data is correct, prepares the data for invocation,
		 * then gets a RestTemplate for the given accessToken and invokes the
		 * provider for the query.
		 */
		Map<String, Object> query = getQuery(true);
		doReturn(null).when(this.discoveryClient).getInstances(anyString());
		doReturn(this.restTemplate).when(this.oauth2Util).getOAuth2RestTemplateForToken(accessToken);
		doReturn(null).when(this.restTemplate).getForObject(anyString(), eq(Map.class));
		this.frontServicesImpl.query(email, "test", accessToken, query);

		verify(this.discoveryClient).getInstances(anyString());
		verify(this.restTemplate).getForObject(anyString(), eq(Map.class));
		verify(this.oauth2Util).getOAuth2RestTemplateForToken(accessToken);
		verifyNoMoreInteractions(this.restTemplate, this.oauth2Util, this.discoveryClient);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#query(java.lang.String, org.springframework.security.oauth2.common.OAuth2AccessToken, java.util.Map)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
	public void testQueryHistoricalNoDate() throws Exception {
		/*
		 * Throws exception if the data contains historical true but date is
		 * empty.
		 */
		testQueryWithFieldRemoved("date");
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#query(java.lang.String, org.springframework.security.oauth2.common.OAuth2AccessToken, java.util.Map)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
	public void testQueryNoExpected() throws Exception {
		/*
		 * Throws exception if expected is null
		 */
		testQueryWithFieldRemoved("expected");
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#query(java.lang.String, org.springframework.security.oauth2.common.OAuth2AccessToken, java.util.Map)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
	public void testQueryNoSource() throws Exception {
		/*
		 * Throws exception if expected is null
		 */
		testQueryWithFieldRemoved("source");
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#query(java.lang.String, org.springframework.security.oauth2.common.OAuth2AccessToken, java.util.Map)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
	public void testQueryNoProvider() throws Exception {
		/*
		 * Throws exception if expected is null
		 */
		testQueryWithFieldRemoved("provider");
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#query(java.lang.String, org.springframework.security.oauth2.common.OAuth2AccessToken, java.util.Map)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
	public void testQueryProviderException() throws Exception {
		/*
		 * Throws exception if expected is null
		 */
		Map<String, Object> query = getQuery(true);
		doReturn(this.restTemplate).when(this.oauth2Util).getOAuth2RestTemplateForToken(accessToken);
		doThrow(RuntimeException.class).when(this.restTemplate).getForObject(anyString(), eq(Map.class));
		this.frontServicesImpl.query(email, "test", accessToken, query);
	}

	/**
	 * Gets a simple services list
	 */
	private List<String> getServicesList() {
		List<String> services = new ArrayList<String>();
		services.add("s1");
		services.add("s2");
		services.add("s3");
		return services;
	}

	/**
	 * Gets a simple query
	 * 
	 * @param isHistorical
	 * @return
	 */
	private Map<String, Object> getQuery(boolean isHistorical) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("provider", "provider");
		map.put("source", "source");
		map.put("historical", isHistorical);
		if (isHistorical) {
			map.put("date", "2000-06-08");
		}
		String expected = "e1,e2,e3";
		map.put("expected", expected);
		return map;

	}

	/**
	 * Tests the query with the given field removed
	 * 
	 * @param fieldName
	 */
	private void testQueryWithFieldRemoved(String fieldName) {
		Map<String, Object> query = getQuery(true);
		query.remove(fieldName);
		this.frontServicesImpl.query(email, "test", accessToken, query);
	}
}
