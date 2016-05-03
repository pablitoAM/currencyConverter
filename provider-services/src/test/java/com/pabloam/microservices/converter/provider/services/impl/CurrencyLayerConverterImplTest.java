/**
 * Copyright (c) 2016 Molenaar Strategie BV.
 * Created: 3 May 2016 09:52:53 Author: Pablo
 */

package com.pabloam.microservices.converter.provider.services.impl;

import static org.junit.Assert.*;
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
import com.pabloam.microservices.converter.common.ConvertedResponse;
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
	public void testConvert() throws Exception {
		/*
		 * Receives a json string. With the object mapper, tries to convert it
		 * to a map. Once the data is in a map, gets the values to convert it
		 * into a ConvertedResponse
		 */
		Map<String, Object> convertedMap = getConvertedMap(false);

		ConvertedResponse actual = testDefaultConvert(convertedMap);
		assertFalse(actual.isHistorical());

		// The date must be null
		assertNull(actual.getHistoryDate());
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

		ConvertedResponse actual = testDefaultConvert(convertedMap);
		assertTrue(actual.isHistorical());

		// The date has been parsed into a localDate
		assertNotNull(actual.getHistoryDate());
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
	private ConvertedResponse testDefaultConvert(Map<String, Object> convertedMap) throws IOException, JsonParseException, JsonMappingException {

		doReturn(convertedMap).when(this.mapper).readValue(eq(jsonResponse), any(TypeReference.class));

		ConvertedResponse actual = this.currencyLayerConverterImpl.convert(jsonResponse);

		// Verification
		verify(this.mapper).readValue(eq(jsonResponse), any(TypeReference.class));
		verifyNoMoreInteractions(this.mapper);

		assertEquals(convertedMap.get("source"), actual.getSourceCurrency());
		assertEquals(convertedMap.get("timestamp"), actual.getTimestamp());

		// The quotes have been polished, but the size must be the same
		@SuppressWarnings("unchecked")
		Map<String, Float> quotesMap = (Map<String, Float>) convertedMap.get("quotes");
		assertEquals(quotesMap.size(), actual.getQuotes().size());

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

		map.put("source", "USD");
		map.put("timestamp", Instant.now().toEpochMilli());

		Map<String, Float> quotesMap = new HashMap<String, Float>();
		quotesMap.put("USDABC", 3.456f);
		quotesMap.put("USDBCD", 7.890f);

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
