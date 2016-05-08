package com.pabloam.microservices.converter.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Pablo
 *
 */
@Configuration
public class ProviderServicesConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
