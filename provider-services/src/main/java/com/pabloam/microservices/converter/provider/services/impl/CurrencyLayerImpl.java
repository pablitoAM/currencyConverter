/**
 * 
 */
package com.pabloam.microservices.converter.provider.services.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.pabloam.microservices.converter.common.ConvertedResponse;
import com.pabloam.microservices.converter.common.RefreshIntervalEnum;
import com.pabloam.microservices.converter.provider.exceptions.RequestException;
import com.pabloam.microservices.converter.provider.services.ConverterServices;
import com.pabloam.microservices.converter.provider.services.ProviderServices;

/**
 * @author PabloAM
 *
 */
@Service
@Profile("currencylayer")
public class CurrencyLayerImpl implements ProviderServices {

	protected static final String URL = "http://apilayer.net/api/live";

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(CurrencyLayerImpl.class);

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
	 * The api key for the invokations
	 */
	@Value("${provider.api.key}")
	protected String apiKey;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ConverterServices converterServices;

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
	 * getRefreshInterval()
	 */
	@Override
	public RefreshIntervalEnum getRefreshInterval() {
		return this.refreshInterval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.provider.services.ProviderServices#
	 * getCurrentRates(java.lang.String, java.lang.String[])
	 */
	@Override
	public ConvertedResponse getCurrentRates(String sourceCurrency, String... expectedCurrencies) {
		try {

			verifyExpectedCurrencies(expectedCurrencies);
			verifySourceCurrency(sourceCurrency);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("access_key", this.apiKey)
					.queryParam("source ", sourceCurrency).queryParam("currencies ",
							Arrays.asList(expectedCurrencies).stream().collect(Collectors.joining(", ")));

			HttpEntity<?> entity = new HttpEntity<>(headers);

			ResponseEntity<Map> response = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET,
					entity, Map.class);

			ConvertedResponse convertedResponse = this.converterServices.convert(response.getBody());
			return convertedResponse;

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
	public ConvertedResponse getHistoricalRates(String sourceCurrency, LocalDate date, String... expectedCurrencies) {
		// TODO Auto-generated method stub
		return null;
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
			throw new IllegalArgumentException(
					"The list of expected currencies must at least contain one currency to convert to.");
		}
	}

}
