/**
 * 
 */
package com.pabloam.microservices.converter.front.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.pabloam.microservices.converter.front.exceptions.BadCredentialsException;
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
	private OAuth2RestTemplate restTemplate;

	@Mock
	private DiscoveryClient discoveryClient;

	@InjectMocks
	private FrontServicesImpl frontServicesImpl;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
		 * Test method for
		 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getOAuth2RestTemplateForUserPassword(java.lang.String, java.lang.String)}
		 * .
		 */
		@Test
		public void testGetOAuth2RestTemplateForUserPassword() throws Exception {
			/*
			 * Verify the credentials are not null nor empty and validate oauthUtil
			 * is invoked.
			 */
			String email = "asdw";
			String password = "test";
			OAuth2RestTemplate oAuth2RestTemplate = mock(OAuth2RestTemplate.class);
	
			doReturn(oAuth2RestTemplate).when(this.oauth2Util).getOAuth2RestTemplateForPassword(email, password);
			doReturn(null).when(oAuth2RestTemplate).getAccessToken();
	
			this.frontServicesImpl.getOAuth2RestTemplateForUserPassword(email, password);
			verify(this.oauth2Util).getOAuth2RestTemplateForPassword(email, password);
			verifyNoMoreInteractions(this.oauth2Util);
	
		}

	/**
		 * Test method for
		 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getOAuth2RestTemplateForUserPassword(java.lang.String, java.lang.String)}
		 * .
		 */
		@Test(expected = BadCredentialsException.class)
		public void testGetOAuth2RestTemplateForUserPasswordWrongPassword() throws Exception {
			/*
			 * Throws exception when the password is null or empty
			 */
			this.frontServicesImpl.getOAuth2RestTemplateForUserPassword("emai", null);
	
		}

	/**
		 * Test method for
		 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getOAuth2RestTemplateForUserPassword(java.lang.String, java.lang.String)}
		 * .
		 */
		@Test(expected = BadCredentialsException.class)
		public void testGetOAuth2RestTemplateForUserPasswordWrongEmail() throws Exception {
			/*
			 * Throws exception when the email is null or empty
			 */
			this.frontServicesImpl.getOAuth2RestTemplateForUserPassword(null, "password");
		}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#register(java.util.Map)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterEmptyMap() throws Exception {
		/*
		 * Throws exception when the user to register is empty
		 */
		this.frontServicesImpl.register(null);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#register(java.util.Map)}
	 * .
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = FrontServicesException.class)
	public void testRegisterRestTemplateException() throws Exception {
		/*
		 * Throws exception when the user to register is empty
		 */
		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("test", "test");

		doThrow(new RuntimeException("RestTemplateException")).when(this.restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(RequestEntity.class),
				any(Class.class));
		this.frontServicesImpl.register(testMap);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#register(java.util.Map)}
	 * .
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = FrontServicesException.class)
	public void testRegisterBadResponse() throws Exception {
		/*
		 * Throws exception when the restTemplate responds with a 4xx or 5xx
		 */
		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("test", "test");
		ResponseEntity<Map> response = mock(ResponseEntity.class);

		doReturn(response).when(this.restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(RequestEntity.class), any(Class.class));
		doReturn(HttpStatus.BAD_GATEWAY).when(response).getStatusCode();
		this.frontServicesImpl.register(testMap);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#register(java.util.Map)}
	 * .
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRegister() throws Exception {
		/*
		 * Verify the invokation to the restTemplate with the given data is
		 * done.
		 */

		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("test", "test");

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ResponseEntity<Map> response = mock(ResponseEntity.class);

		doReturn(this.restTemplate).when(this.oauth2Util).getOAuth2RestTemplateForClientCredentials();
		doReturn(response).when(this.restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(RequestEntity.class), any(Class.class));
		doReturn(HttpStatus.ACCEPTED).when(response).getStatusCode();

		this.frontServicesImpl.register(testMap);
		verify(this.restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(RequestEntity.class), any(Class.class));
		verify(this.oauth2Util).getOAuth2RestTemplateForClientCredentials();
		verifyNoMoreInteractions(this.restTemplate, this.oauth2Util);
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

		this.frontServicesImpl.providerPrefix = "p";

		doReturn(services).when(this.discoveryClient).getServices();
		List<String> activeProviders = this.frontServicesImpl.getActiveProviders();

		verify(this.discoveryClient).getServices();
		verifyNoMoreInteractions(this.discoveryClient);

		assertEquals(2, activeProviders.size());
		assertTrue(activeProviders.contains("1"));
		assertTrue(activeProviders.contains("2"));

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

		this.frontServicesImpl.providerPrefix = "p";

		doReturn(services).when(this.discoveryClient).getServices();
		List<String> activeProviders = this.frontServicesImpl.getActiveProviders();

		verify(this.discoveryClient).getServices();
		verifyNoMoreInteractions(this.discoveryClient);

		assertEquals(0, activeProviders.size());
	}

	/*
	 * Gets a simple services list
	 */
	private List<String> getServicesList() {
		List<String> services = new ArrayList<String>();
		services.add("s1");
		services.add("s2");
		services.add("s3");
		return services;
	}

}
