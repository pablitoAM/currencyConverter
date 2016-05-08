package com.pabloam.microservices.converter.provider.services;

import java.util.Map;

import com.pabloam.microservices.converter.provider.exceptions.RequestException;

public interface ProviderServices {

	/**
	 * Returns the provider name
	 * 
	 * @return
	 */
	public String getProviderName();

	/**
	 * /** Returns a Map<String, Object> with the current rates for the given
	 * sourceCurrency
	 * 
	 * @param sourceCurrency
	 *            the source currency acronym to query for
	 * @param expectedCurrencies
	 *            the expected currency acronyms
	 * @return
	 * @throws RequestException
	 */
	public Map<String, Object> getCurrentRates(String sourceCurrency, String... expectedCurrencies) throws RequestException;

	/**
	 * Returns a Map<String, Object> with the historical rates for the given
	 * sourceCurrency
	 * 
	 * @param sourceCurrency
	 *            the source currency acronym to query for
	 * @param date
	 *            the date to check
	 * @param expectedCurrencies
	 *            the expected currency acronyms
	 * @return
	 * @throws RequestException
	 */
	public Map<String, Object> getHistoricalRates(String sourceCurrency, String date, String... expectedCurrencies) throws RequestException;

}
