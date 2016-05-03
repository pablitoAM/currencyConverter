package com.pabloam.microservices.converter.common;

import java.util.Map;

/**
 * @author Pablo
 *
 */
public interface CurrencyQuery {

	/**
	 * Returns the username of the query
	 * 
	 * @return
	 */
	public String getUsername();

	/**
	 * Returns the source currency of the query
	 * 
	 * @return
	 */
	public String getSourceCurrency();

	/**
	 * Returns the timestamp of the query
	 * 
	 * @return
	 */
	public Long getTimestamp();

	/**
	 * Returns the quotes of the query
	 * 
	 * @return
	 */
	public Map<String, Double> getQuotes();

	/**
	 * Returns true if the query was historical
	 * 
	 * @return
	 */
	public boolean isHistorical();

	/**
	 * Returns the date of the historical query if it was, if not returns null
	 * 
	 * @return
	 */
	public String getHistoricalDate();

	/**
	 * Returns the provider of the query
	 * 
	 * @return
	 */
	public String getProvider();

	/**
	 * Returns the creation date of the query in the database
	 * 
	 * @return
	 */
	public Long getCreated();

}
