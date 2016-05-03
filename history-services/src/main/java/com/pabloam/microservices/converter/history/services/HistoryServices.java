package com.pabloam.microservices.converter.history.services;

import java.util.List;

import com.pabloam.microservices.converter.common.CurrencyQuery;

public interface HistoryServices {

	/**
	 * Returns the last number queries for the given user
	 * 
	 * @param number
	 * @param userName
	 * @return
	 */
	public List<CurrencyQuery> getLastQueriesOf(int number, String userName);

}
