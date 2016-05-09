package com.pabloam.microservices.converter.history.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;

/**
 * @author Pablo
 *
 */
@Configuration
@Profile("test")
public class FongoTestConfiguration extends AbstractMongoConfiguration {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(MongoConfiguration.class);

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongo(), getDatabaseName());
	}

	@Override
	protected String getDatabaseName() {
		return "inMemoryMongoDB";
	}

	@Override
	public Mongo mongo() {
		// uses fongo for in-memory tests
		return new Fongo("inMemoryMongoDB").getMongo();
	}

	@Override
	protected String getMappingBasePackage() {
		return "com.pabloam.microservices.converter.history.model";
	}

	@Value("${mongodb.database:testDB}")
	private String database;

}
