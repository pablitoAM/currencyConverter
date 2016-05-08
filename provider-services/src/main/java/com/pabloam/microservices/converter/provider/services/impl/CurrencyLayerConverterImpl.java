package com.pabloam.microservices.converter.provider.services.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pabloam.microservices.converter.provider.exceptions.ConversionException;
import com.pabloam.microservices.converter.provider.services.ConverterServices;

@Component
@Profile("currencylayer")
public class CurrencyLayerConverterImpl implements ConverterServices {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(CurrencyLayerConverterImpl.class);

	/**
	 * The object mappper to parse the json
	 */
	protected ObjectMapper mapper;

	/**
	 * Init method to create an instance of the object mapper
	 */
	@PostConstruct
	public void init() {
		this.mapper = new ObjectMapper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.provider.services.ConverterServices#
	 * convert(java.lang.String)
	 */
	@Override
	public Map<String, Object> convert(String jsonResponse) throws ConversionException {

		try {

			if (!StringUtils.hasText(jsonResponse)) {
				throw new IllegalArgumentException("The response to convert cannot be empty.");
			}

			TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
			};

			HashMap<String, Object> responseMap = mapper.readValue(jsonResponse, typeRef);

			Map<String, Object> response = processMap(responseMap);
			return response;

		} catch (Exception e) {
			logger.error("Exception converting the following response '{}'", jsonResponse, e);
			throw new ConversionException(String.format("Exception converting the following response: '%s' - %s", jsonResponse, e.getMessage()));
		}

	}

	// ===============================
	// Private Methods
	// ===============================

	/**
	 * Converts the map into a converted response
	 * 
	 * @param responseMap
	 * @return
	 */
	private Map<String, Object> processMap(HashMap<String, Object> responseMap) {

		Map<String, Object> response = new HashMap<String, Object>();

		// Success?
		boolean success = (boolean) responseMap.getOrDefault("success", false);
		if (!success) {
			@SuppressWarnings("unchecked")
			Map<String, Object> errorMap = (Map<String, Object>) responseMap.get("error");
			String errorInfo = null;
			if (!CollectionUtils.isEmpty(errorMap)) {
				errorInfo = errorMap.containsKey("info") ? (String) errorMap.get("info") : "Unknown error";
			}
			throw new ConversionException(String.format("Error in response: '%s'", errorInfo));
		}

		// Source Currency
		String source = (String) responseMap.get("source");
		if (StringUtils.hasText(source)) {
			response.put("source", source);
		} else {
			throw new ConversionException("The response must contain a source currency");
		}

		// Timestamp
		Long timestamp = (Long) responseMap.get("timestamp");
		if (timestamp != null) {
			response.put("timestamp", timestamp);
		} else {
			throw new ConversionException("The response must contain a timestamp");
		}

		// Quotes
		@SuppressWarnings("unchecked")
		Map<String, Double> quotes = (Map<String, Double>) responseMap.get("quotes");
		if (!CollectionUtils.isEmpty(quotes)) {
			// Polish quotes removing the leading source acronyms.
			Map<String, Double> polishedQuotes = quotes.entrySet().stream().collect(Collectors.toMap(e -> polishKey(source, e.getKey()), e -> e.getValue()));
			response.put("quotes", polishedQuotes);
		} else {
			throw new ConversionException("The response must contain quotes");
		}

		// Historical
		boolean historical = (boolean) responseMap.getOrDefault("historical", false);
		response.put("historical", historical);
		if (historical) {

			// HistoricalDate
			String historicalDateString = (String) responseMap.get("date");
			if (StringUtils.hasText(historicalDateString)) {
				response.put("historicalDate", LocalDate.parse(historicalDateString));
			} else {
				throw new ConversionException("If the response is historical it must contain a date");
			}

		}
		return response;
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
