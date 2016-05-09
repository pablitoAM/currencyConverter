package com.pabloam.microservices.converter.provider.services.impl;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.pabloam.microservices.converter.provider.exceptions.ConversionException;
import com.pabloam.microservices.converter.provider.services.ConverterServices;

@Component
@Profile("currencylayer")
public class CurrencyLayerConverterImpl implements ConverterServices {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(CurrencyLayerConverterImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.provider.services.ConverterServices#
	 * convert(java.lang.String)
	 */
	@Override
	public Map<String, Object> convert(Map<String, Object> response) throws ConversionException {

		try {
			return processMap(response);

		} catch (Exception e) {
			logger.error("Response '{}'", response, e);
			throw new ConversionException(String.format("'%s' - %s", response, e.getMessage()));
		}

	}

	// ===============================
	// Private Methods
	// ===============================

	/**
	 * Converts the map into a converted response
	 * 
	 * @param response
	 * @return
	 */
	private Map<String, Object> processMap(Map<String, Object> response) {

		if (CollectionUtils.isEmpty(response)) {
			throw new ConversionException("The response to process cannot be empty");
		}

		Map<String, Object> processedResponse = new HashMap<String, Object>();

		// Success?
		boolean success = (boolean) response.getOrDefault("success", false);
		if (!success) {
			@SuppressWarnings("unchecked")
			Map<String, Object> errorMap = (Map<String, Object>) response.get("error");
			String errorInfo = null;
			if (!CollectionUtils.isEmpty(errorMap)) {
				errorInfo = errorMap.containsKey("info") ? (String) errorMap.get("info") : "Unknown error";
			}
			throw new ConversionException(String.format("%s", errorInfo));
		}

		// Source Currency
		String source = (String) response.get("source");
		if (StringUtils.hasText(source)) {
			processedResponse.put("source", source);
		} else {
			throw new ConversionException("The response must contain a source currency");
		}

		// Timestamp
		Object ts = response.get("timestamp");
		if (ts != null) {
			if (ts instanceof Integer) {
				processedResponse.put("timestamp", ((Integer) ts).longValue());
			} else if (ts instanceof Long) {
				processedResponse.put("timestamp", ts);
			}
		} else {
			throw new ConversionException("The response must contain a timestamp");
		}

		// Quotes
		@SuppressWarnings("unchecked")
		Map<String, Object> quotes = (Map<String, Object>) response.get("quotes");
		if (!CollectionUtils.isEmpty(quotes)) {
			// Polish quotes removing the leading source acronyms.
			Map<String, Double> polishedQuotes = quotes.entrySet().stream().map(e -> polishAndParse(source, e))
					.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
			processedResponse.put("quotes", polishedQuotes);
		} else {
			throw new ConversionException("The response must contain quotes");
		}

		// Historical
		boolean historical = (boolean) response.getOrDefault("historical", false);
		processedResponse.put("historical", historical);
		if (historical) {

			// HistoricalDate
			String historicalDateString = (String) response.get("date");
			if (StringUtils.hasText(historicalDateString)) {
				processedResponse.put("historicalDate", LocalDate.parse(historicalDateString));
			} else {
				throw new ConversionException("If the response is historical it must contain a date");
			}

		}
		return processedResponse;
	}

	private Map.Entry<String, Double> polishAndParse(String source, Entry<String, Object> e) {
		Double value = e.getValue() instanceof Double ? (double) e.getValue() : (double) ((Integer) e.getValue()).intValue();
		return new AbstractMap.SimpleEntry<String, Double>(polishKey(source, e.getKey()), value);
	}

	/**
	 * Removes the sourceCurrency prefix in the keyToPolish
	 * 
	 * @param key
	 * @return
	 */
	private String polishKey(String sourceCurrency, String keyToPolish) {
		return keyToPolish.replaceFirst("^" + sourceCurrency, "");
	}

}
