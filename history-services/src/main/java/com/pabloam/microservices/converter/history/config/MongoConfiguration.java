package com.pabloam.microservices.converter.history.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * @author Pablo
 *
 */
@Configuration
@Profile("mongodb-prod")
@EnableMongoRepositories
public class MongoConfiguration extends AbstractMongoConfiguration {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(MongoConfiguration.class);

	@Value("${mongodb.database}")
	private String database;

	@Value("${mongodb.url}")
	private String mongodbUrl;

	@Override
	protected String getDatabaseName() {
		return this.database;
	}

	@Override
	public Mongo mongo() throws Exception {
		MongoClientURI uri = new MongoClientURI(mongodbUrl);
		return new MongoClient(uri);
	}
}
