/**
 * 
 */
package com.pabloam.microservices.converter.provider.services.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.pabloam.microservices.converter.common.RefreshIntervalEnum;
import com.pabloam.microservices.converter.provider.exceptions.RequestException;
import com.pabloam.microservices.converter.provider.services.ConverterServices;

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

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.CurrencyLayerImpl#getProviderName()}
	 * .
	 */
	@Test
	public void testGetProviderName() throws Exception {
		/*
		 * When invoked must return the provider name
		 */
		String expected = this.currencyLayerImpl.providerName;

		String actual = this.currencyLayerImpl.getProviderName();
		assertEquals(expected, actual);

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.CurrencyLayerImpl#getRefreshInterval()}
	 * .
	 */
	@Test
	public void testGetRefreshInterval() throws Exception {
		/*
		 * When invoked must return the refresh interval
		 */
		RefreshIntervalEnum expected = this.currencyLayerImpl.refreshInterval;

		RefreshIntervalEnum actual = this.currencyLayerImpl.getRefreshInterval();
		assertEquals(expected, actual);
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

		this.currencyLayerImpl.getCurrentRates(sourceCurrency, expected1, expected2);

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
		 * When invoked verify that the response is not a 4xx or 5xx, if so,
		 * then throw exception
		 */

//		ResponseEntity<String> mockedResponse = mock(ResponseEntity.class);
//		doReturn(mockedResponse).when(this.restTemplate);
		this.currencyLayerImpl.getCurrentRates(sourceCurrency, expected1, expected2);
	}

	@Test(expected = RequestException.class)
	public void testGetCurrentRatesWrongConversion() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.provider.services.impl.CurrencyLayerImpl#getHistoricalRates(java.lang.String, java.time.LocalDate, java.lang.String[])}
	 * .
	 */
	@Test
	public void testGetHistoricalRates() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

}
