package com.pabloam.microservices.converter.history.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoDatabase;

/**
 * @author Pablo
 *
 */
@Configuration
@Profile("test")
public class FongoTestConfiguration {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(MongoConfiguration.class);

	@Value("${mongodb.database:testDB}")
	private String database;

	@Bean
	Fongo mongoClient() {
		return new Fongo("inMemoryMongoDB");
	}

	@Bean
	MongoDatabase db(Fongo fongo) {
		return fongo.getDatabase(database);
	}

}
