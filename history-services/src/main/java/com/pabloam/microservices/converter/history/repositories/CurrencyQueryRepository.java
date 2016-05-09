package com.pabloam.microservices.converter.history.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pabloam.microservices.converter.history.model.CurrencyQuery;

/**
 * @author Pablo
 *
 */
@Profile("mongodb")
public interface CurrencyQueryRepository {

	/**
	 * Returns the list of the last number queries for the given userName
	 * 
	 * @param number
	 * @param userName
	 * @return
	 */
	public List<CurrencyQuery> getLastQueriesOf(int number, String userName);

	/**
	 * Saves a currencyQuery into the database
	 */
	public void saveCurrencyQuery(CurrencyQuery currencyQuery);

}
