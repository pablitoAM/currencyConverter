package com.pabloam.microservices.converter.history.repositories.mongo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.pabloam.microservices.converter.history.model.CurrencyQuery;
import com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository;

/**
 * @author Pablo
 *
 */
@Repository
@Profile("mongodb")
public class MongoCurrencyQueryRepository implements CurrencyQueryRepository {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(MongoCurrencyQueryRepository.class);

	protected static final String QUERY_COLLECTION = "currencyQueries";

	/**
	 * The mongo database
	 */
	@Autowired
	protected MongoTemplate mongodb;

	/**
	 * @see com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository#getLastQueriesOf(int,
	 *      java.lang.String)
	 */
	@Override
	public List<CurrencyQuery> getLastQueriesOf(int number, String email) {
		// @formatter:off
		Query query = new Query(Criteria.where("email").is(email)).with(new Sort(Sort.Direction.DESC, "crated")).limit(number);
		List<CurrencyQuery> collection = mongodb.find(query, CurrencyQuery.class);
		// @formatter:on
		return collection;
	}

	/**
	 * @see com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository#saveCurrencyQuery(java.lang.String,
	 *      java.lang.String, java.util.Map)
	 */
	@Override
	public void saveCurrencyQuery(CurrencyQuery currencyQuery) {
		mongodb.save(currencyQuery, QUERY_COLLECTION);
	}
}
