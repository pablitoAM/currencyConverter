package com.pabloam.microservices.converter.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableDiscoveryClient
public class ProviderServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderServicesApplication.class, args);
	}
}
