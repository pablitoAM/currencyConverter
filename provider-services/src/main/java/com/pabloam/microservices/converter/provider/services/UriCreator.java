package com.pabloam.microservices.converter.provider.services;

import java.net.URI;

/**
 * @author Pablo
 *
 */
public interface UriCreator {

	/**
	 * Creates a current uri
	 * 
	 * @param baseUrl
	 * @param apiKey
	 * @param sourceCurrency
	 * @param expectedCurrencies
	 * @return
	 */
	public URI createCurrentUri(String baseUrl, String apiKey, String sourceCurrency, String... expectedCurrencies);

	/**
	 * Creates an historical uri
	 * 
	 * @param baseUrl
	 * @param apiKey
	 * @param sourceCurrency
	 * @param date
	 * @param expectedCurrencies
	 * @return
	 */
	public URI createHistoricalUri(String baseUrl, String apiKey, String sourceCurrency, String date, String... expectedCurrencies);

}
