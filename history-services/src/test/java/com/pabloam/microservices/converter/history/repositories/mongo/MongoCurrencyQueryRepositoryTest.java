/**
 * Copyright (c) 2016 Molenaar Strategie BV.
 * Created: 3 May 2016 17:17:56 Author: Pablo
 */

package com.pabloam.microservices.converter.history.repositories.mongo;

import static com.mongodb.client.model.Sorts.descending;
import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pabloam.microservices.converter.history.config.FongoTestConfiguration;

/**
 * @author Pablo
 *
 */
@ActiveProfiles({ "test" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { FongoTestConfiguration.class })
public class MongoCurrencyQueryRepositoryTest {

	/**
	 * The in-memory database
	 */
	@Autowired
	private MongoDatabase fongodb;

	/**
	 * The class to test
	 */
	private MongoCurrencyQueryRepository repository;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.repository = new MongoCurrencyQueryRepository(fongodb);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.repositories.mongo.MongoCurrencyQueryRepository#getLastQueriesOf(int, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetLastQueriesOf() throws Exception {
		/*
		 * Insert multiple for a user, and retrieve the last ten by creation
		 * date
		 */
		String userName = "Test user";
		String provider = "Random provider";

		int maxQueriesToInsert = 25;
		int numberOfQueriesToRetrieve = 10;

		List<Document> queriesToInsert = new ArrayList<Document>(maxQueriesToInsert);
		List<Map<String, Object>> expected = new ArrayList<Map<String, Object>>(numberOfQueriesToRetrieve);

		for (int i = maxQueriesToInsert - 1; i >= 0; i--) {
			Document currencyQuery = (Document) getCurrencyQueryDocument(true);

			// Add userName, provider and created timestamp
			currencyQuery.put("userName", userName);
			currencyQuery.put("provider", provider);
			currencyQuery.put("created", Instant.now().toEpochMilli());
			queriesToInsert.add(currencyQuery);

			if (i < numberOfQueriesToRetrieve) {
				expected.add(currencyQuery);
			}

		}

		// Insert them into mongo
		MongoCollection<Document> collection = fongodb.getCollection(MongoCurrencyQueryRepository.QUERY_COLLECTION);
		collection.insertMany(queriesToInsert);

		List<Map<String, Object>> lastQueriesOf = this.repository.getLastQueriesOf(numberOfQueriesToRetrieve, userName);

		// Verify last ten
		assertEquals(numberOfQueriesToRetrieve, lastQueriesOf.size());

		lastQueriesOf.stream().forEach(query -> {
			
			// Verify the queries are the expected
			
			
		});

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.repositories.mongo.MongoCurrencyQueryRepository#saveCurrencyQuery(java.util.Map)}
	 * .
	 */
	@Test
	public void testSaveCurrencyQuery() throws Exception {
		/*
		 * Insert a received currency query adding the userName, provider and
		 * the current timestamp. Then verify it has been inserted
		 */
		String userName = "Test user";
		String provider = "Random provider";

		Map<String, Object> currencyQuery = getCurrencyQueryDocument(true);

		this.repository.saveCurrencyQuery(userName, provider, currencyQuery);

		// Verify it exists
		ArrayList<Map<String, Object>> itemFound = fongodb.getCollection(MongoCurrencyQueryRepository.QUERY_COLLECTION).find(new Document("userName", userName))
				.sort(descending("created")).limit(1).into(new ArrayList<Map<String, Object>>());

		assertEquals(itemFound.size(), 1);

		Map<String, Object> item = itemFound.get(0);

		assertEquals(userName, item.get("userName"));
		assertEquals(provider, item.get("provider"));
		currencyQuery.entrySet().stream().forEach(entry -> {

			// Verify

			assertEquals(entry.getValue(), item.get(entry.getKey()));
		});
	}

	/**
	 * Creates a currencyQuery map
	 * 
	 * @return
	 */
	private Map<String, Object> getCurrencyQueryDocument(boolean isHistorical) {
		Map<String, Object> map = new Document();

		map.put("success", true);

		map.put("source", "EUR");
		map.put("timestamp", Instant.now().toEpochMilli());

		Map<String, Double> quotesMap = new HashMap<String, Double>();
		quotesMap.put("ABC", 3.456);
		quotesMap.put("BCD", 7.890);

		map.put("quotes", quotesMap);

		if (isHistorical) {
			map.put("historical", true);
			map.put("date", "2016-05-02");
		}

		return map;

	}

}
