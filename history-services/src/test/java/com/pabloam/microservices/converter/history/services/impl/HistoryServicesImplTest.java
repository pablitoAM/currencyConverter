/**
 * 
 */
package com.pabloam.microservices.converter.history.services.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pabloam.microservices.converter.history.exceptions.HistoryServiceException;
import com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository;

/**
 * @author PabloAM
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HistoryServicesImplTest {

	@Mock
	private CurrencyQueryRepository currencyQueryRepository;

	@InjectMocks
	private HistoryServicesImpl historyServicesImpl;

	private String provider;
	private String userName;
	private Map<String, Object> currencyQuery;

	private int number;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		this.provider = "Random Provider";
		this.userName = "Test User";
		this.currencyQuery = getCurrencyQueryMap(true);
		this.number = 10;

	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.services.impl.HistoryServicesImpl#getLastQueriesOf(int, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetLastQueriesOf() throws Exception {
		/*
		 * We verify the repository save method is invoked with the correct
		 * parameters
		 */
		doReturn(null).when(this.currencyQueryRepository).getLastQueriesOf(number, userName);
		this.historyServicesImpl.getLastQueriesOf(number, userName);

		verify(this.currencyQueryRepository).getLastQueriesOf(number, userName);
		verifyNoMoreInteractions(this.currencyQueryRepository);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.services.impl.HistoryServicesImpl#getLastQueriesOf(int, java.lang.String)}
	 * .
	 */
	@Test(expected = HistoryServiceException.class)
	public void testGetLastQueriesOfEmptyUserName() throws Exception {
		/*
		 * Throws exception if the username is null or empty.
		 */
		this.historyServicesImpl.getLastQueriesOf(number, null);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.services.impl.HistoryServicesImpl#getLastQueriesOf(int, java.lang.String)}
	 * .
	 */
	@Test(expected = HistoryServiceException.class)
	public void testGetLastQueriesOfWrongNumber() throws Exception {
		/*
		 * Throws exception if the number is not positive
		 */
		this.historyServicesImpl.getLastQueriesOf(-5, userName);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.services.impl.HistoryServicesImpl#save(java.lang.String, java.lang.String, java.util.Map)}
	 * .
	 */
	@Test
	public void testSave() throws Exception {
		/*
		 * We verify the repository save method is invoked with the correct
		 * parameters
		 */

		doNothing().when(this.currencyQueryRepository).saveCurrencyQuery(userName, provider, currencyQuery);

		this.historyServicesImpl.save(provider, userName, currencyQuery);

		verify(this.currencyQueryRepository).saveCurrencyQuery(userName, provider, currencyQuery);
		verifyNoMoreInteractions(this.currencyQueryRepository);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.services.impl.HistoryServicesImpl#save(java.lang.String, java.lang.String, java.util.Map)}
	 * .
	 */
	@Test(expected = HistoryServiceException.class)
	public void testSaveEmptyCurrencyQuery() throws Exception {
		/*
		 * Throws exception when currencyQuery is null or empty
		 */
		this.historyServicesImpl.save(provider, userName, null);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.services.impl.HistoryServicesImpl#save(java.lang.String, java.lang.String, java.util.Map)}
	 * .
	 */
	@Test(expected = HistoryServiceException.class)
	public void testSaveEmptyProvider() throws Exception {
		/*
		 * Throws exception when provider is null or empty
		 */
		this.historyServicesImpl.save(null, userName, currencyQuery);
	}

	/**
	 * Test method for
	 * {@link com.pabloam.microservices.converter.history.services.impl.HistoryServicesImpl#save(java.lang.String, java.lang.String, java.util.Map)}
	 */
	@Test(expected = HistoryServiceException.class)
	public void testSaveEmptyUserName() throws Exception {
		/*
		 * Throws exception when userName is null or empty
		 */
		this.historyServicesImpl.save(provider, null, currencyQuery);
	}

	/**
	 * Creates a currencyQuery map
	 * 
	 * @return
	 */
	private Map<String, Object> getCurrencyQueryMap(boolean isHistorical) {
		Map<String, Object> map = new Document();

		map.put("source", "EUR");
		map.put("timestamp", Instant.now().toEpochMilli());

		Map<String, Double> quotesMap = new HashMap<String, Double>();
		quotesMap.put("ABC", 3.456);
		quotesMap.put("BCD", 7.890);

		map.put("quotes", quotesMap);

		if (isHistorical) {
			map.put("historical", true);
			map.put("date", "2016-05-02");
		}

		return map;

	}

}
