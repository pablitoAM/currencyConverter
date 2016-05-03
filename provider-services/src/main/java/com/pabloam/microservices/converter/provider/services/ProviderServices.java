package com.pabloam.microservices.converter.provider.services;

import com.pabloam.microservices.converter.common.ConvertedResponse;
import com.pabloam.microservices.converter.common.RefreshIntervalEnum;
import com.pabloam.microservices.converter.provider.exceptions.RequestException;

public interface ProviderServices {

	/**
	 * Returns the provider name
	 * 
	 * @return
	 */
	public String getProviderName();

	/**
	 * Returns the refresh period of the provider
	 * 
	 * @return
	 */
	public RefreshIntervalEnum getRefreshInterval();

	/**
	 * /** Returns a convertedResponse with the current rates for the given
	 * sourceCurrency
	 * 
	 * @param sourceCurrency
	 *            the source currency acronym to query for
	 * @param expectedCurrencies
	 *            the expected currency acronyms
	 * @return
	 * @throws RequestException
	 */
	public ConvertedResponse getCurrentRates(String sourceCurrency, String... expectedCurrencies) throws RequestException;

	/**
	 * Returns a convertedResponse with the historical rates for the given
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
	public ConvertedResponse getHistoricalRates(String sourceCurrency, String date, String... expectedCurrencies) throws RequestException;

}
