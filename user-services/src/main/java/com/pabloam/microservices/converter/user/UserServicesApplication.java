package com.pabloam.microservices.converter.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.SessionAttributes;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAuthorizationServer
public class UserServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServicesApplication.class, args);
	}
}
