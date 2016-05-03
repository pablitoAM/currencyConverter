/**
 * Copyright (c) 2016 Molenaar Strategie BV.
 * Created: 3 May 2016 15:23:18 Author: Pablo
 */

package com.pabloam.microservices.converter.history.repositories.mongo;

import static com.mongodb.client.model.Sorts.descending;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository;

/**
 * @author Pablo
 *
 */
@Repository
@Profile("mongodb")
public class MongoCurrencyQueryRepository implements CurrencyQueryRepository {

	protected static final String QUERY_COLLECTION = "currencyQueries";

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(MongoCurrencyQueryRepository.class);

	/**
	 * The mongo database
	 */
	private MongoDatabase mongodb;

	/**
	 * The constructor which autowires the database
	 * 
	 * @param mongodb
	 */
	@Autowired
	public MongoCurrencyQueryRepository(MongoDatabase mongodb) {
		this.mongodb = mongodb;
	}

	/**
	 * @see com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository#getLastQueriesOf(int,
	 *      java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getLastQueriesOf(int number, String userName) {
		// @formatter:off
		List<Map<String, Object>> collection = mongodb.getCollection(QUERY_COLLECTION).find(new Document("userName", userName)).sort(descending("created")).limit(number)
				.into(new ArrayList<Map<String, Object>>());
		// @formatter:on
		return collection;
	}

	/**
	 * @see com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository#saveCurrencyQuery(java.lang.String,
	 *      java.lang.String, java.util.Map)
	 */
	@Override
	public void saveCurrencyQuery(String userName, String provider, Map<String, Object> currencyQuery) {

		MongoCollection<Document> collection = mongodb.getCollection(QUERY_COLLECTION);

		Document document = createDocument(userName, provider, currencyQuery);
		collection.insertOne(document);
	}

	// ===============================
	// Private Methods
	// ===============================

	/**
	 * @param userName
	 * @param provider
	 * @param currencyQuery
	 * @return
	 */
	private Document createDocument(String userName, String provider, Map<String, Object> currencyQuery) {
		Document doc = new Document("userName", userName);
		doc.append("provider", provider);
		doc.append("created", Instant.now(Clock.systemUTC()).toEpochMilli());
		currencyQuery.entrySet().stream().forEach(entry -> doc.append(entry.getKey(), entry.getValue()));
		return doc;
	}

}
