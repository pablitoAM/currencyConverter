package com.pabloam.microservices.converter.provider.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pabloam.microservices.converter.provider.exceptions.ConversionException;

/**
 * @author Pablo
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CurrencyLayerConverterImplTest {

	@InjectMocks
	private CurrencyLayerConverterImpl currencyLayerConverterImpl;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.CurrencyLayerConverterImpl#convert(java.lang.String)}
	 * .
	 */
	@Test
	public void testConvertCurrent() throws Exception {
		/*
		 * Receives a json string. With the object mapper, tries to convert it
		 * to a map.
		 */
		Map<String, Object> convertedMap = getConvertedMap(false);

		Map<String, Object> actual = testDefaultConvert(convertedMap);
		assertFalse((Boolean) actual.get("historical"));

		// The date must be null
		assertNull(actual.get("date"));
	}

	@Test
	public void testConvertHistorical() throws Exception {
		/*
		 * Receives a json string. With the object mapper, tries to convert it
		 * to a map. Once the data is in a map, gets the values to convert it
		 * into a ConvertedResponse. Verifies that isHistorical is true and has
		 * a date
		 */
		Map<String, Object> convertedMap = getConvertedMap(true);

		Map<String, Object> actual = testDefaultConvert(convertedMap);
		assertTrue((Boolean) actual.get("historical"));

		// The date has been parsed into a localDate
		assertNotNull(actual.get("date"));
	}

	/**
	 * Tests that the mapper is invoked and the common fields are converted.
	 * Returns the actual result for further validation
	 * 
	 * @param jsonResponse
	 * @param convertedMap
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private Map<String, Object> testDefaultConvert(Map<String, Object> response) throws IOException, JsonParseException, JsonMappingException {

		Map<String, Object> actual = this.currencyLayerConverterImpl.convert(response);

		// Verification
		assertEquals(response.get("source"), actual.get("source"));
		assertEquals(response.get("timestamp"), actual.get("timestamp"));

		// The quotes have been polished, but the size must be the same
		Map<String, Double> quotesMap = (Map<String, Double>) response.get("quotes");
		Map<String, Double> actualQuotesMap = (Map<String, Double>) actual.get("quotes");
		assertEquals(quotesMap.size(), actualQuotesMap.size());

		return actual;
	}

	@Test(expected = ConversionException.class)
	public void testConvertNoQuotes() throws Exception {
		/*
		 * Throws exception when the response contains no quotes
		 */
		Map<String, Object> response = getConvertedMap(true);
		response.remove("quotes");
		this.currencyLayerConverterImpl.convert(response);
	}

	@Test(expected = ConversionException.class)
	public void testConvertNoSource() throws Exception {
		/*
		 * Throws exception when the response contains no source
		 */
		Map<String, Object> response = getConvertedMap(true);
		response.remove("source");
		this.currencyLayerConverterImpl.convert(response);
	}

	@Test(expected = ConversionException.class)
	public void testConvertHistoricalNoDate() throws Exception {
		/*
		 * Throws exception when the response is historical but contains no date
		 */
		Map<String, Object> response = getConvertedMap(true);
		response.remove("date");
		this.currencyLayerConverterImpl.convert(response);
	}

	@Test(expected = ConversionException.class)
	public void testConvertNoSuccess() throws Exception {
		/*
		 * Throws exception when the response is an error message
		 */
		Map<String, Object> response = getErrorMap();
		this.currencyLayerConverterImpl.convert(response);

	}

	@Test(expected = ConversionException.class)
	public void testConvertExceptionEmptyResponse() throws Exception {
		/*
		 * If the input string is wrong, throws a conversionException
		 */
		this.currencyLayerConverterImpl.convert(null);
	}

	// ===============================
	// Private Methods
	// ===============================

	/**
	 * Creates a successful response map
	 * 
	 * @param isHistorical
	 *            if true it will include historical and date
	 * @return
	 */
	private Map<String, Object> getConvertedMap(boolean isHistorical) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("success", true);

		map.put("source", "EUR");
		map.put("timestamp", Instant.now().toEpochMilli());

		Map<String, Double> quotesMap = new HashMap<String, Double>();
		quotesMap.put("EURABC", 3.456);
		quotesMap.put("EURBCD", 7.890);

		map.put("quotes", quotesMap);

		if (isHistorical) {
			map.put("historical", true);
			map.put("date", "2016-05-02");
		}

		return map;
	}

	/**
	 * Creates a failed response map
	 * 
	 * @return
	 */
	private Map<String, Object> getErrorMap() {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);

		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("code", 104);
		errorMap.put("info", "Error message");

		map.put("error", errorMap);

		return map;

	}

}
