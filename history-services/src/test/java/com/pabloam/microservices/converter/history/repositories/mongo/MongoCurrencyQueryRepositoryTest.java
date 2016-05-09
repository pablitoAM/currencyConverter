package com.pabloam.microservices.converter.history.repositories.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import com.pabloam.microservices.converter.history.config.FongoTestConfiguration;
import com.pabloam.microservices.converter.history.model.CurrencyQuery;
import com.pabloam.microservices.converter.history.model.Quote;

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
	private MongoTemplate mongodb;

	/**
	 * The class to test
	 */
	private MongoCurrencyQueryRepository repository;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.repository = new MongoCurrencyQueryRepository();
		this.repository.mongodb = this.mongodb;

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.repositories.mongo.MongoCurrencyQueryRepository#getLastQueriesOf(int, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetLastQueriesOf() throws Exception {
		/*
		 * Insert multiple queries for a user, and retrieve the last ten by
		 * creation date. To prove it we store the last ten creation dates in a
		 * list. Then we check if the retrieved queries have exactly the same
		 * creation dates
		 */
		String userName = "Test user";
		String provider = "Random provider";

		int maxQueriesToInsert = 25;
		int numberOfQueriesToRetrieve = 10;

		List<CurrencyQuery> queriesToInsert = new ArrayList<CurrencyQuery>(maxQueriesToInsert);
		List<Long> expectedCreationDates = new ArrayList<Long>(numberOfQueriesToRetrieve);

		for (int i = maxQueriesToInsert - 1; i >= 0; i--) {
			CurrencyQuery currencyQuery = getCurrencyQuery(true);

			// Add userName, provider and created timestamp
			currencyQuery.setEmail(userName);
			currencyQuery.setProvider(provider);
			currencyQuery.setCreated(Instant.now().toEpochMilli());
			queriesToInsert.add(currencyQuery);

			if (i < numberOfQueriesToRetrieve) {
				expectedCreationDates.add((Long) currencyQuery.getCreated());
			}

		}

		// Insert them into mongo
		mongodb.insert(queriesToInsert, CurrencyQuery.class);

		List<CurrencyQuery> lastQueriesOf = this.repository.getLastQueriesOf(numberOfQueriesToRetrieve, userName);

		// Verify last ten
		assertEquals(expectedCreationDates.size(), lastQueriesOf.size());

		lastQueriesOf.stream().forEach(e -> {
			// Verify the queries are the expected
			assertTrue(expectedCreationDates.contains(e.getCreated()));
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

		CurrencyQuery currencyQuery = getCurrencyQuery(true);
		currencyQuery.setEmail(userName);
		currencyQuery.setProvider(provider);
		currencyQuery.setCreated(Instant.now().toEpochMilli());

		this.repository.saveCurrencyQuery(currencyQuery);

		// Verify it exists
		Query query = new Query(Criteria.where("email").is(userName)).with(new Sort(Sort.Direction.DESC, "created")).limit(1);
		List<CurrencyQuery> list = mongodb.find(query, CurrencyQuery.class);

		Assert.assertTrue(!CollectionUtils.isEmpty(list));

		CurrencyQuery itemFound = list.get(0);
		assertNotNull(itemFound);

		assertEquals(userName, itemFound.getEmail());
		assertEquals(provider, itemFound.getProvider());
	}

	/**
	 * Creates a currencyQuery map
	 * 
	 * @return
	 */
	private CurrencyQuery getCurrencyQuery(boolean isHistorical) {
		CurrencyQuery cq = new CurrencyQuery();

		cq.setSource("EUR");
		cq.setTimestamp(Instant.now().toEpochMilli());

		List<Quote> quotesList = new ArrayList<Quote>();
		Quote q = new Quote();
		q.setCurrency("ABC");
		q.setValue(3.456);

		Quote q2 = new Quote();
		q2.setCurrency("CVD");
		q2.setValue(2.4546);
		quotesList.add(q);
		quotesList.add(q2);

		if (isHistorical) {
			cq.setHistorical(isHistorical);
			cq.setDate("2016-05-02");
		}

		return cq;

	}

}
