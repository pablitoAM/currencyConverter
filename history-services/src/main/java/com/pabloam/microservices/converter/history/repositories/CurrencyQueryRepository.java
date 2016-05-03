/**
 * Copyright (c) 2016 Molenaar Strategie BV.
 * Created: 3 May 2016 15:53:01 Author: Pablo
 */

package com.pabloam.microservices.converter.history.repositories;

import java.util.List;
import java.util.Map;

/**
 * @author Pablo
 *
 */
public interface CurrencyQueryRepository {

	/**
	 * Returns the list of the last number queries for the given userName
	 * 
	 * @param number
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getLastQueriesOf(int number, String userName);

	/**
	 * Saves the given map with the given userName and provider into the
	 * database
	 * 
	 * @param userName
	 * @param provider
	 * @param currencyQuery
	 */
	public void saveCurrencyQuery(String userName, String provider, Map<String, Object> currencyQuery);

}
