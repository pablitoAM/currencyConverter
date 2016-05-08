package com.pabloam.microservices.converter.provider.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pabloam.microservices.converter.provider.exceptions.ConversionException;

/**
 * @author Pablo
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CurrencyLayerConverterImplTest {

	@Mock
	private ObjectMapper mapper;

	@InjectMocks
	private CurrencyLayerConverterImpl currencyLayerConverterImpl;

	private String jsonResponse;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.jsonResponse = "expectedRightJsonResponse";
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
		assertNull(actual.get("historicalDate"));
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
		assertNotNull(actual.get("historicalDate"));
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
	private Map<String, Object> testDefaultConvert(Map<String, Object> convertedMap) throws IOException, JsonParseException, JsonMappingException {

		doReturn(convertedMap).when(this.mapper).readValue(eq(jsonResponse), any(TypeReference.class));
		Map<String, Object> actual = this.currencyLayerConverterImpl.convert(jsonResponse);

		// Verification
		verify(this.mapper).readValue(eq(jsonResponse), any(TypeReference.class));
		verifyNoMoreInteractions(this.mapper);

		assertEquals(convertedMap.get("source"), actual.get("source"));
		assertEquals(convertedMap.get("timestamp"), actual.get("timestamp"));

		// The quotes have been polished, but the size must be the same
		Map<String, Double> quotesMap = (Map<String, Double>) convertedMap.get("quotes");
		Map<String, Double> actualQuotesMap = (Map<String, Double>) actual.get("quotes");
		assertEquals(quotesMap.size(), actualQuotesMap.size());

		return actual;
	}

	@Test(expected = ConversionException.class)
	public void testConvertNoQuotes() throws Exception {
		/*
		 * Throws exception when the response contains no quotes
		 */
		Map<String, Object> convertedMap = getConvertedMap(true);
		convertedMap.remove("quotes");

		doReturn(convertedMap).when(this.mapper).readValue(eq(jsonResponse), any(TypeReference.class));

		this.currencyLayerConverterImpl.convert(jsonResponse);
	}

	@Test(expected = ConversionException.class)
	public void testConvertNoSource() throws Exception {
		/*
		 * Throws exception when the response contains no source
		 */
		Map<String, Object> convertedMap = getConvertedMap(true);
		convertedMap.remove("source");

		doReturn(convertedMap).when(this.mapper).readValue(eq(jsonResponse), any(TypeReference.class));

		this.currencyLayerConverterImpl.convert(jsonResponse);
	}

	@Test(expected = ConversionException.class)
	public void testConvertHistoricalNoDate() throws Exception {
		/*
		 * Throws exception when the response is historical but contains no date
		 */
		Map<String, Object> convertedMap = getConvertedMap(true);
		convertedMap.remove("date");

		doReturn(convertedMap).when(this.mapper).readValue(eq(jsonResponse), any(TypeReference.class));

		this.currencyLayerConverterImpl.convert(jsonResponse);
	}

	@Test(expected = ConversionException.class)
	public void testConvertNoSuccess() throws Exception {
		/*
		 * Throws exception when the response is an error message
		 */
		Map<String, Object> convertedMap = getErrorMap();
		doReturn(convertedMap).when(this.mapper).readValue(eq(jsonResponse), any(TypeReference.class));

		this.currencyLayerConverterImpl.convert(jsonResponse);

	}

	@Test(expected = ConversionException.class)
	public void testConvertExceptionEmptyResponse() throws Exception {
		/*
		 * If the input string is wrong, throws a conversionException
		 */
		this.currencyLayerConverterImpl.convert("");
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
