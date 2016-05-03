package com.pabloam.microservices.converter.history;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class HistoryServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistoryServicesApplication.class, args);
	}
}
