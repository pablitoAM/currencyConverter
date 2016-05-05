/**
 * 
 */
package com.pabloam.microservices.converter.front.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.RestTemplate;

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
	private RestTemplate restTemplate;

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
		OAuth2RestTemplate oAuth2RestTemplate = mock(OAuth2RestTemplate.class);

		doReturn(oAuth2RestTemplate).when(this.oauth2Util).getOAuth2RestTemplateForPassword(email, password);
		doReturn(null).when(oAuth2RestTemplate).getAccessToken();

		this.frontServicesImpl.getAccessToken(email, password);
		verify(this.oauth2Util).getOAuth2RestTemplateForPassword(email, password);
		verifyNoMoreInteractions(this.oauth2Util);

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getAccessToken(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = BadCredentialsException.class)
	public void testGetAccessTokenWrongPassword() throws Exception {
		/*
		 * Throws exception when the password is null or empty
		 */
		this.frontServicesImpl.getAccessToken("emai", null);

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#getAccessToken(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test(expected = BadCredentialsException.class)
	public void testGetAccessTokenWrongEmail() throws Exception {
		/*
		 * Throws exception when the email is null or empty
		 */
		this.frontServicesImpl.getAccessToken(null, "password");
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#register(java.util.Map)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
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
	@Test(expected = FrontServicesException.class)
	public void testRegisterRestTemplateException() throws Exception {
		/*
		 * Throws exception when the user to register is empty
		 */
		Map<String, String> testMap = new HashMap<String, String>();

		doThrow(new RuntimeException("RestTemplateException")).when(this.restTemplate).postForEntity(anyString(), any(), any());
		this.frontServicesImpl.register(testMap);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#register(java.util.Map)}
	 * .
	 */
	@Test(expected = FrontServicesException.class)
	public void testRegisterBadResponse() throws Exception {
		/*
		 * Throws exception when the restTemplate responds with a 4xx or 5xx
		 */
		Map<String, String> testMap = new HashMap<String, String>();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ResponseEntity<Map> response = mock(ResponseEntity.class);

		doReturn(response).when(this.restTemplate).postForEntity(anyString(), any(), any());
		doReturn(HttpStatus.BAD_GATEWAY).when(response).getStatusCode();
		this.frontServicesImpl.register(testMap);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.front.services.impl.FrontServicesImpl#register(java.util.Map)}
	 * .
	 */
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

		doReturn(response).when(this.restTemplate).postForEntity(anyString(), any(), any());
		doReturn(HttpStatus.ACCEPTED).when(response).getStatusCode();

		this.frontServicesImpl.register(testMap);
		verify(this.restTemplate).postForEntity(anyString(), any(), any());
		verifyNoMoreInteractions(this.restTemplate);
	}

}
