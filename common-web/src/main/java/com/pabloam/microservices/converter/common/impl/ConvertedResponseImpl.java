/**
 * 
 */
package com.pabloam.microservices.converter.common.impl;

import java.time.LocalDate;
import java.util.Map;

import com.pabloam.microservices.converter.common.ConvertedResponse;

/**
 * @author PabloAM
 *
 */
public class ConvertedResponseImpl implements ConvertedResponse {

	/**
	 * The source currency
	 */
	private String sourceCurrency;

	/**
	 * The quotes for the expected currencies
	 */
	private Map<String, Double> quotes;

	/**
	 * The request timestamp
	 */
	private Long timestamp;

	/**
	 * True if the request was historical
	 */
	private boolean isHistorical;

	/**
	 * The date the historical request was made. If not historical will be null
	 */
	private LocalDate historyDate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pabloam.microservices.converter.common.ConvertedResponse#
	 * getSourceCurrency()
	 */
	public String getSourceCurrency() {
		return this.sourceCurrency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.common.ConvertedResponse#getQuotes(
	 * java.lang.String[])
	 */
	public Map<String, Double> getQuotes() {
		return this.quotes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.common.ConvertedResponse#getTimestamp
	 * ()
	 */
	public Long getTimestamp() {
		return this.timestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.common.ConvertedResponse#isHistorical
	 * ()
	 */
	public boolean isHistorical() {
		return this.isHistorical;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pabloam.microservices.converter.common.ConvertedResponse#
	 * getHistoryDate()
	 */
	public LocalDate getHistoryDate() {
		return this.historyDate;
	}

	public void setQuotes(Map<String, Double> quotes) {
		this.quotes = quotes;
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public void setHistorical(boolean isHistorical) {
		this.isHistorical = isHistorical;
	}

	public void setHistoryDate(LocalDate historyDate) {
		this.historyDate = historyDate;
	}

}
