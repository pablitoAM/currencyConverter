package com.pabloam.microservices.converter.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableOAuth2Client
public class WebFrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFrontApplication.class, args);
	}
}
