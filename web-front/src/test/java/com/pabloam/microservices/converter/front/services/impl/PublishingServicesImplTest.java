/**
 * 
 */
package com.pabloam.microservices.converter.front.services.impl;

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
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.pabloam.microservices.converter.front.exceptions.PublishingException;
import com.pabloam.microservices.converter.front.web.util.OAuth2Util;

/**
 * @author PabloAM
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PublishingServicesImplTest {

	@Mock
	private DiscoveryClient discoveryClient;

	@Mock
	private OAuth2Util oauth2Util;

	@InjectMocks
	private PublishingServicesImpl publishingServicesImpl;

	@Mock
	private OAuth2AccessToken accessToken;

	@Mock
	private OAuth2RestTemplate restTemplate;

	private String user = "user";
	private String provider = "provider";
	private Map<String, Object> query = new HashMap<String, Object>();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.publishingServicesImpl.historyServicesClientId = "test-service";
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.PublishingServicesImpl#publishInHistoryServices(java.lang.String, java.lang.String, java.util.Map)}
	 * .
	 */
	@Test
	public void testPublishInHistoryServices() throws Exception {
		/*
		 * Get the history-services instance with the discovery client and try
		 * to invoke for post with the restTemplate.
		 */
		List<ServiceInstance> instances = new ArrayList<ServiceInstance>(1);
		ServiceInstance instance = mock(ServiceInstance.class);
		instances.add(instance);

		doReturn(restTemplate).when(this.oauth2Util).getOAuth2RestTemplateForToken(accessToken);
		doReturn(instances).when(discoveryClient).getInstances(anyString());
		doReturn(true).when(this.restTemplate).postForObject(anyString(), any(Map.class), eq(Boolean.class));
		this.publishingServicesImpl.publishInHistoryServices(user, provider, accessToken, query);

		verify(this.oauth2Util).getOAuth2RestTemplateForToken(accessToken);
		verify(this.discoveryClient).getInstances(anyString());
		verify(this.restTemplate).postForObject(anyString(), any(Map.class), eq(Boolean.class));
		verifyNoMoreInteractions(this.discoveryClient, this.restTemplate, this.oauth2Util);

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.PublishingServicesImpl#publishInHistoryServices(java.lang.String, java.lang.String, java.util.Map)}
	 * .
	 */
	@Test(expected = PublishingException.class)
	public void testPublishInHistoryServicesRestTemplateException() throws Exception {
		/*
		 * Throws exception if the restTemplate invocation throws exception
		 */
		List<ServiceInstance> instances = new ArrayList<ServiceInstance>(1);
		ServiceInstance instance = mock(ServiceInstance.class);
		instances.add(instance);
		doReturn(instances).when(discoveryClient).getInstances(anyString());
		doThrow(RuntimeException.class).when(this.restTemplate).postForObject(anyString(), any(Map.class), eq(Boolean.class));
		this.publishingServicesImpl.publishInHistoryServices(user, provider, accessToken, query);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.PublishingServicesImpl#publishInHistoryServices(java.lang.String, java.lang.String, java.util.Map)}
	 * .
	 */
	@Test(expected = PublishingException.class)
	public void testPublishInHistoryServicesDiscoveryException() throws Exception {
		/*
		 * Throws exception if the discoveryClient invocation throws exception
		 */
		doThrow(RuntimeException.class).when(this.discoveryClient).getInstances(anyString());
		this.publishingServicesImpl.publishInHistoryServices(user, provider, accessToken, query);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.PublishingServicesImpl#publishInHistoryServices(java.lang.String, java.lang.String, java.util.Map)}
	 * .
	 */
	@Test(expected = PublishingException.class)
	public void testPublishInHistoryServicesNoServiceException() throws Exception {
		/*
		 * Throws exception if the restTemplate invokation throws exception
		 */
		List<ServiceInstance> instances = new ArrayList<ServiceInstance>(1);
		doReturn(instances).when(discoveryClient).getInstances(anyString());
		this.publishingServicesImpl.publishInHistoryServices(user, provider, accessToken, query);
	}

}
