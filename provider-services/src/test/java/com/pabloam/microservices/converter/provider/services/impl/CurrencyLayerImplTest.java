/**
 * 
 */
package com.pabloam.microservices.converter.provider.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.pabloam.microservices.converter.provider.exceptions.ConversionException;
import com.pabloam.microservices.converter.provider.exceptions.RequestException;
import com.pabloam.microservices.converter.provider.model.RefreshIntervalEnum;
import com.pabloam.microservices.converter.provider.services.ConverterServices;
import com.pabloam.microservices.converter.provider.services.UriCreator;

/**
 * @author PabloAM
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CurrencyLayerImplTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ConverterServices converterServices;

	@Mock
	private UriCreator uriCreator;

	@InjectMocks
	private CurrencyLayerImpl currencyLayerImpl;

	String sourceCurrency;
	String expected1;
	String expected2;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.sourceCurrency = "SRC";
		this.expected1 = "EXP1";
		this.expected2 = "EXP2";

		currencyLayerImpl.providerName = "CurrencyLayerTest";
		currencyLayerImpl.refreshInterval = RefreshIntervalEnum.HOURLY;
		currencyLayerImpl.apiKey = "12345";
		currencyLayerImpl.apiUrl = "http://go.com";
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.CurrencyLayerImpl#getCurrentRates(java.lang.String, java.lang.String[])}
	 * .
	 */
	@Test
	public void testGetCurrentRates() throws Exception {
		/*
		 * When invoked verify all the parameters are right. Then verify the
		 * invocation with the restTemplate is made. And then verify the
		 * invocation to the converter is right.
		 */
		@SuppressWarnings("unchecked")
		ResponseEntity<Map> mockedResponse = mock(ResponseEntity.class);
		Map<String, Object> convertedRespose = new HashMap<String, Object>();
		Map<String, Object> originalResponse = new HashMap<String, Object>();
		URI uri = URI.create("randomUri");

		doReturn(uri).when(this.uriCreator).createCurrentUri(currencyLayerImpl.apiUrl, currencyLayerImpl.apiKey, sourceCurrency, expected1, expected2);
		doReturn(originalResponse).when(mockedResponse).getBody();
		doReturn(HttpStatus.ACCEPTED).when(mockedResponse).getStatusCode();

		doReturn(mockedResponse).when(this.restTemplate).exchange(eq(uri), eq(HttpMethod.GET), any(RequestEntity.class), eq(Map.class));
		doReturn(convertedRespose).when(this.converterServices).convert(originalResponse);

		this.currencyLayerImpl.getCurrentRates(sourceCurrency, expected1, expected2);

		verify(this.uriCreator).createCurrentUri(currencyLayerImpl.apiUrl, currencyLayerImpl.apiKey, sourceCurrency, expected1, expected2);
		verify(this.restTemplate).exchange(eq(uri), eq(HttpMethod.GET), any(RequestEntity.class), eq(Map.class));
		verify(this.converterServices).convert(originalResponse);
		verifyNoMoreInteractions(this.restTemplate, this.converterServices, this.uriCreator);

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.CurrencyLayerImpl#getHistoricalRates(java.lang.String, java.time.LocalDate, java.lang.String[])}
	 * .
	 */
	@Test
	public void testGetHistoricalRates() throws Exception {

		/*
		 * When invoked verify all the parameters are right. Then verify the
		 * invocation with the restTemplate is made. And then verify the
		 * invocation to the converter is right.
		 */
		@SuppressWarnings("unchecked")
		ResponseEntity<Map> mockedResponse = mock(ResponseEntity.class);
		Map<String, Object> convertedRespose = new HashMap<String, Object>();
		Map<String, Object> originalResponse = new HashMap<String, Object>();
		URI uri = URI.create("randomUri");

		String date = "2016-12-03";

		doReturn(uri).when(this.uriCreator).createHistoricalUri(currencyLayerImpl.apiUrl, currencyLayerImpl.apiKey, sourceCurrency, date, expected1, expected2);
		doReturn(originalResponse).when(mockedResponse).getBody();
		doReturn(HttpStatus.ACCEPTED).when(mockedResponse).getStatusCode();

		doReturn(mockedResponse).when(this.restTemplate).exchange(eq(uri), eq(HttpMethod.GET), any(RequestEntity.class), eq(Map.class));
		doReturn(convertedRespose).when(this.converterServices).convert(originalResponse);

		this.currencyLayerImpl.getHistoricalRates(sourceCurrency, date, expected1, expected2);

		verify(this.uriCreator).createHistoricalUri(currencyLayerImpl.apiUrl, currencyLayerImpl.apiKey, sourceCurrency, date, expected1, expected2);
		verify(this.restTemplate).exchange(eq(uri), eq(HttpMethod.GET), any(RequestEntity.class), eq(Map.class));
		verify(this.converterServices).convert(originalResponse);
		verifyNoMoreInteractions(this.restTemplate, this.converterServices, this.uriCreator);

	}

	@Test(expected = RequestException.class)
	public void testGetHistoricalRatesInvalidDate() throws Exception {
		/*
		 * Throws exception when the date is not valid
		 */
		this.currencyLayerImpl.getHistoricalRates(sourceCurrency, "2016-13-31", expected1, expected2);
	}

	@Test(expected = RequestException.class)
	public void testGetCurrentRatesNoSourceCurrency() throws Exception {
		/*
		 * When invoked verify that sourceCurrency cannot be null nor empty and
		 * throw exception in that case.
		 */
		this.currencyLayerImpl.getCurrentRates(null, expected1, expected2);
	}

	@Test(expected = RequestException.class)
	public void testGetCurrentRatesNoExpectedCurrencies() throws Exception {
		/*
		 * When invoked verify that expectedCurrencies cannot be null nor empty
		 * and throw exception in that case.
		 */
		this.currencyLayerImpl.getCurrentRates(sourceCurrency, null);
	}

	@Test(expected = RequestException.class)
	public void testGetCurrentRatesWrongResponse() throws Exception {
		/*
		 * Throws exception when the response status is 4xx or 5xx
		 */

		ResponseEntity<Map> mockedResponse = mock(ResponseEntity.class);
		doReturn(mockedResponse).when(this.restTemplate).exchange(any(URI.class), eq(HttpMethod.GET), any(RequestEntity.class), eq(Map.class));
		doReturn(HttpStatus.NOT_FOUND).when(mockedResponse).getStatusCode();

		this.currencyLayerImpl.getCurrentRates(sourceCurrency, expected1, expected2);

	}

	@Test(expected = RequestException.class)
	public void testGetCurrentRatesWrongConversion() throws Exception {
		/*
		 * Throws exception when the response body is not as expected
		 */
		Map<String, Object> originalResponse = new HashMap<String, Object>();

		ResponseEntity<Map> mockedResponse = mock(ResponseEntity.class);
		doReturn(mockedResponse).when(this.restTemplate).exchange(any(URI.class), eq(HttpMethod.GET), any(RequestEntity.class), eq(Map.class));
		doReturn(HttpStatus.ACCEPTED).when(mockedResponse).getStatusCode();
		doReturn(originalResponse).when(mockedResponse).getBody();
		doThrow(ConversionException.class).when(this.converterServices).convert(originalResponse);

		this.currencyLayerImpl.getCurrentRates(sourceCurrency, expected1, expected2);
	}

}
