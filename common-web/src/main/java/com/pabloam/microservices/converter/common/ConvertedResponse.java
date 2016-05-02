package com.pabloam.microservices.converter.common;

import java.time.LocalDate;
import java.util.Map;

/**
 * 
 * @author Pablo
 *
 */
public interface ConvertedResponse {

	/**
	 * Returns the acronym of the source currency of the response
	 * 
	 * @return
	 */
	public String getSource();

	/**
	 * Returns a map with the quotes for the given expectedCurrencies
	 * 
	 * @param currencies
	 *            the currencies expected as result
	 * @return
	 */
	public Map<String, Float> getQuotes(String... expectedCurrencies);

	/**
	 * The timestamp returned from the provider
	 * 
	 * @return
	 */
	public Long getTimestamp();

	/**
	 * Returns true if the query was historical
	 * 
	 * @return
	 */
	public boolean isHistorical();

	/**
	 * Returns the date for given for the historical query. If not, returns null
	 * 
	 * @return
	 */
	public LocalDate getDate();

}
