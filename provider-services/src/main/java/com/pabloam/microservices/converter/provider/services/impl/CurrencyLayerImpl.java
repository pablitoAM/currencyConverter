/**
 * 
 */
package com.pabloam.microservices.converter.provider.services.impl;

import java.net.URI;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.pabloam.microservices.converter.provider.exceptions.BadResponseException;
import com.pabloam.microservices.converter.provider.exceptions.RequestException;
import com.pabloam.microservices.converter.provider.model.RefreshIntervalEnum;
import com.pabloam.microservices.converter.provider.services.ConverterServices;
import com.pabloam.microservices.converter.provider.services.ProviderServices;
import com.pabloam.microservices.converter.provider.services.UriCreator;

/**
 * @author PabloAM
 *
 */
@Component
@Profile("currencylayer")
public class CurrencyLayerImpl implements ProviderServices {

	protected static final String URL = "http://apilayer.net/api/live";

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(CurrencyLayerImpl.class);

	// ===============================
	// Properties
	// ===============================

	/**
	 * The name of the provider
	 */
	@Value("${provider.name}")
	protected String providerName;

	/**
	 * The refresh interval of the provider
	 */
	@Value("${provider.refresh.interval}")
	protected RefreshIntervalEnum refreshInterval;

	/**
	 * The api key for the provider
	 */
	@Value("${provider.api.key}")
	protected String apiKey;

	/**
	 * The restTemplate for REST invocations
	 */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * The services in charge of converting the response
	 */
	@Autowired
	private ConverterServices converterServices;

	/**
	 * The uri creator for the invocations
	 */
	@Autowired
	private UriCreator uriCreator;

	// ===============================
	// Implementation
	// ===============================

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.provider.services.ProviderServices#
	 * getProviderName()
	 */
	@Override
	public String getProviderName() {
		return this.providerName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.provider.services.ProviderServices#
	 * getCurrentRates(java.lang.String, java.lang.String[])
	 */
	@Override
	public Map<String, Object> getCurrentRates(String sourceCurrency, String... expectedCurrencies) {
		try {

			verifyExpectedCurrencies(expectedCurrencies);
			verifySourceCurrency(sourceCurrency);

			URI uri = this.uriCreator.createCurrentUri(URL, this.apiKey, sourceCurrency, expectedCurrencies);

			// Invocation to the API
			ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.GET, getRequestEntity(), String.class);

			verifyResponse(response);

			return Collections.singletonMap("success", this.converterServices.convert(response.getBody()));

		} catch (Exception e) {
			logger.error("Exception in getCurrentRates[{}]: {}", this.providerName, e.getMessage(), e);
			throw new RequestException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.provider.services.ProviderServices#
	 * getHistoricalRates(java.lang.String, java.time.LocalDate,
	 * java.lang.String[])
	 */
	@Override
	public Map<String, Object> getHistoricalRates(String sourceCurrency, String date, String... expectedCurrencies) {
		try {
			verifySourceCurrency(sourceCurrency);
			verifyExpectedCurrencies(expectedCurrencies);
			verifyDate(date);

			URI uri = this.uriCreator.createHistoricalUri(URL, this.apiKey, sourceCurrency, date, expectedCurrencies);

			// Invocation to the API
			ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.GET, getRequestEntity(), String.class);

			verifyResponse(response);

			return Collections.singletonMap("success", this.converterServices.convert(response.getBody()));

		} catch (Exception e) {
			logger.error("Exception in getHistoricalRates[{}]: {}", this.providerName, e.getMessage(), e);
			throw new RequestException(e.getMessage(), e);
		}

	}

	// ===============================
	// Private Methods
	// ===============================

	/**
	 * Prepares the request entity for JSON
	 * 
	 * @return
	 */
	private HttpEntity<?> getRequestEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		return entity;
	}

	/**
	 * Verifies if the date is in a YYYY-MM-DD format
	 * 
	 * @param date
	 * @throws ParseException
	 */
	private void verifyDate(String date) {
		try {
			LocalDate.parse(date);
		} catch (DateTimeParseException | NullPointerException e) {
			throw new IllegalArgumentException("The date is not valid.", e);
		}
	}

	/**
	 * Verifies the sourceCurrency is not null nor empty
	 * 
	 * @param sourceCurrency
	 */
	private void verifySourceCurrency(String sourceCurrency) {
		if (!StringUtils.hasText(sourceCurrency)) {
			throw new IllegalArgumentException("The source currency cannot be null.");
		}
	}

	/**
	 * Verifies there is at least one expectedCurrency
	 * 
	 * @param expectedCurrencies
	 */
	private void verifyExpectedCurrencies(String... expectedCurrencies) {
		if (expectedCurrencies == null || expectedCurrencies.length == 0) {
			throw new IllegalArgumentException("The list of expected currencies must at least contain one currency to convert to.");
		}
	}

	/**
	 * Verifies the response is not a 4xx or a 5xx
	 * 
	 * @param response
	 */
	private void verifyResponse(ResponseEntity<String> response) {
		if (response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError()) {
			throw new BadResponseException(
					String.format("The response from the server is: %s - %s", response.getStatusCode(), response.getStatusCode().getReasonPhrase()));
		}
	}

}
