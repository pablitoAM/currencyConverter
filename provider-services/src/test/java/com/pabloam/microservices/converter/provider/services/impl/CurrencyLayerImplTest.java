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
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Verify;
import com.pabloam.microservices.converter.common.RefreshIntervalEnum;

/**
 * @author PabloAM
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CurrencyLayerImplTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private CurrencyLayerImpl currencyLayerImpl;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
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
		 * invocation with the restTemplate is made.
		 */
		String sourceCurrency = "SRC";
		String expected1 = "EXP1";
		String expected2 = "EXP2";

		
		this.currencyLayerImpl.getCurrentRates(sourceCurrency, expected1, expected2);

		
	}

	@Test
	public void testGetCurrentRatesNoExpected	() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testGetCurrentRatesNoSource() throws Exception {
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
