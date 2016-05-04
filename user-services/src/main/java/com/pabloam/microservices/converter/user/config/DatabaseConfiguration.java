package com.pabloam.microservices.converter.user.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("jpa")
@EnableAutoConfiguration
@Configuration
@EntityScan(basePackages = { "com.pabloam.microservices.converter.user.model" })
@EnableJpaRepositories("com.pabloam.microservices.converter.user.repositories")
public class DatabaseConfiguration {

}
