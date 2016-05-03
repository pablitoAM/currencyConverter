package com.pabloam.microservices.converter.history.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * @author Pablo
 *
 */
@Configuration
@Profile("mongodb")
public class MongoConfiguration {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(MongoConfiguration.class);

	@Value("${mongodb.host}")
	private String host;

	@Value("${mongodb.port}")
	private Integer port;

	@Value("${mongodb.username}")
	private String username;

	@Value("${mongodb.database}")
	private String database;

	@Value("${mongodb.password}")
	private String password;

	@Bean
	MongoClient mongoClient() {
		return new MongoClient(host, port);
	}

	@Bean
	MongoDatabase db(MongoClient mongoClient) {
		return mongoClient.getDatabase(database);
	}
}
