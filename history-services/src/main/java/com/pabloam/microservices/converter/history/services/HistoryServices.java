package com.pabloam.microservices.converter.history.services;

import java.util.List;
import java.util.Map;

import com.pabloam.microservices.converter.history.model.CurrencyQuery;

public interface HistoryServices {

	/**
	 * Returns the last number queries for the given user
	 * 
	 * @param number
	 * @param userName
	 * @return
	 */
	public List<CurrencyQuery> getLastQueriesOf(int number, String userName);

	/**
	 * Saves the given currencyQuery in the databese for the given user and for
	 * the given provider.
	 * 
	 * @param provider
	 * @param userName
	 * @param currencyQuery
	 * @return
	 */
	public Boolean save(String provider, String userName, Map<String, Object> currencyQuery);

}
