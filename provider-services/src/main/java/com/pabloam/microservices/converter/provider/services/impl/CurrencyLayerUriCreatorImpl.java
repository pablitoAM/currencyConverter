package com.pabloam.microservices.converter.provider.services.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.pabloam.microservices.converter.provider.services.UriCreator;

/**
 * @author Pablo
 *
 */
@Component
@Profile("currencylayer")
public class CurrencyLayerUriCreatorImpl implements UriCreator {

	/**
	 * @see com.pabloam.microservices.converter.provider.services.UriCreator#createCurrentUri(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public URI createCurrentUri(String baseUrl, String apiKey, String sourceCurrency, String... expectedCurrencies) {
		// @formatter:off
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/live")
				.queryParam("access_key", apiKey)
				.queryParam("source", sourceCurrency)				
				.queryParam("currencies",
						Arrays.asList(expectedCurrencies).stream().collect(Collectors.joining(",")));
		// @formatter:on

		return builder.build().encode().toUri();
	}

	/**
	 * @see com.pabloam.microservices.converter.provider.services.UriCreator#createHistoricalUri(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String[])
	 */
	@Override
	public URI createHistoricalUri(String baseUrl, String apiKey, String sourceCurrency, String date, String... expectedCurrencies) {

		// @formatter:off
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/historical")
				.queryParam("access_key", apiKey)
				.queryParam("source", sourceCurrency)
				.queryParam("date", date)
				.queryParam("currencies",
						Arrays.asList(expectedCurrencies).stream().collect(Collectors.joining(",")));
		// @formatter:on

		return builder.build().encode().toUri();
	}

}
