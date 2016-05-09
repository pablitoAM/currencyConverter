/**
 * 
 */
package com.pabloam.microservices.converter.front.services.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.pabloam.microservices.converter.front.exceptions.PublishingException;
import com.pabloam.microservices.converter.front.services.PublishingServices;
import com.pabloam.microservices.converter.front.web.util.OAuth2Util;

/**
 * @author PabloAM
 *
 */
@Service
public class PublishingServicesImpl implements PublishingServices {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(PublishingServicesImpl.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private OAuth2Util oauth2Util;

	@Value("${history.services.client.id:}")
	protected String historyServicesClientId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.provider.services.PublishingServices#
	 * publishInHistoryServices(java.util.Map)
	 */
	@Override
	public void publishInHistoryServices(String user, String provider, OAuth2AccessToken accessToken, Map<String, Object> response) throws PublishingException {
		String url = null;
		try {
			url = prepareHistoryServicesUrl(user, provider);
			OAuth2RestTemplate restTemplate = oauth2Util.getOAuth2RestTemplateForToken(accessToken);
			Boolean result = restTemplate.postForObject(url, response, Boolean.class);
			if (result) {
				logger.info("Query {} published successfully.", response);
			}
		} catch (Exception e) {
			logger.error("Exception publishing query: {} in {}", response, url, e);
			throw new PublishingException(String.format("Exception publishing query: %s", e.getMessage()));
		}
	}

	/**
	 * 
	 * @param discoveryClient
	 * @param user
	 * @param provider
	 * @return
	 */
	private String prepareHistoryServicesUrl(String user, String provider) {

		List<ServiceInstance> historyServicesInstances = this.discoveryClient.getInstances(historyServicesClientId);
		if (!CollectionUtils.isEmpty(historyServicesInstances)) {
			ServiceInstance serviceInstance = historyServicesInstances.get(0);
			// http://history-services/history/save/{username}/{provider}
			return String.format("%s/history/save?u=%s&p=%s", serviceInstance.getUri(), user, provider);
		}
		throw new PublishingException("There is no instance of history services running");

	}

}
