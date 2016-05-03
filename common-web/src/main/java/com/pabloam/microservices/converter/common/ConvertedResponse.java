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
	public String getSourceCurrency();

	/**
	 * Returns a map with the quotes for the given expectedCurrencies
	 * 
	 * @return
	 */
	public Map<String, Double> getQuotes();

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
	public LocalDate getHistoryDate();

}
