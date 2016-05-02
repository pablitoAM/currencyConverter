package com.pabloam.microservices.converter.provider.services;

import java.time.LocalDate;

import com.pabloam.microservices.converter.common.ConvertedResponse;
import com.pabloam.microservices.converter.common.RefreshPeriodEnum;

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
	public RefreshPeriodEnum getRefreshInterval();

	/**
	 * Returns a convertedResponse with the current rates for the given
	 * sourceCurrency
	 * 
	 * @param sourceCurrency
	 *            the source currency acronym to query for
	 * @param expectedCurrencies
	 *            the expected currency acronyms
	 * @return
	 */
	public ConvertedResponse getCurrentRates(String sourceCurrency, String... expectedCurrencies);

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
	 */
	public ConvertedResponse getHistoricalRates(String sourceCurrency, LocalDate date, String... expectedCurrencies);

}
