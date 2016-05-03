package com.pabloam.microservices.converter.provider.services.impl;

import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Pablo
 *
 */
public class CurrencyLayerUriCreatorImplTest {

	private String baseUrl;
	private String apiKey;
	private String sourceCurrency;
	private String expected1;
	private String expected2;
	private String date;

	private CurrencyLayerUriCreatorImpl uriCreator;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		uriCreator = new CurrencyLayerUriCreatorImpl();

		baseUrl = "http://localhost:1234";
		apiKey = "testKey";
		sourceCurrency = "SRC";
		expected1 = "EXP1";
		expected2 = "EXP2";
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.CurrencyLayerUriCreatorImpl#createCurrentUri(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
	 * .
	 */
	@Test
	public void testCreateCurrentUri() throws Exception {
		/*
		 * Verifies that the created uri contains "live" and the expected
		 * parameters
		 */
		URI actualUri = uriCreator.createCurrentUri(baseUrl, apiKey, sourceCurrency, expected1, expected2);

		String url = actualUri.toString();

		assertTrue(url.contains(baseUrl + "/live"));

		verifyUrlContainsCommonParameters(url);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.CurrencyLayerUriCreatorImpl#createHistoricalUri(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
	 * .
	 */
	@Test
	public void testCreateHistoricalUri() throws Exception {
		/*
		 * Verifies the created uri contains "historical" and the expected
		 * parameters
		 */
		this.date = "2016-05-02";

		URI actualUri = uriCreator.createHistoricalUri(baseUrl, apiKey, sourceCurrency, date, expected1, expected2);

		String url = actualUri.toString();

		assertTrue(url.contains(baseUrl + "/historical"));
		assertTrue(url.contains("date=" + date));

		verifyUrlContainsCommonParameters(url);
	}

	/**
	 * Verifies the url contains access_key, source and currencies expected
	 * 
	 * @param url
	 */
	private void verifyUrlContainsCommonParameters(String url) {
		assertTrue(url.contains("access_key=" + apiKey));
		assertTrue(url.contains("source=" + sourceCurrency));
		assertTrue(url.contains("currencies=" + expected1 + "," + expected2));
	}
}
