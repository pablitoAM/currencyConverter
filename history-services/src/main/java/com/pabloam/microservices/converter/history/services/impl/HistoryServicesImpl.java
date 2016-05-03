package com.pabloam.microservices.converter.history.services.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.pabloam.microservices.converter.history.exceptions.HistoryServiceException;
import com.pabloam.microservices.converter.history.repositories.CurrencyQueryRepository;
import com.pabloam.microservices.converter.history.services.HistoryServices;

/**
 * @author Pablo
 *
 */
@Service
public class HistoryServicesImpl implements HistoryServices {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(HistoryServicesImpl.class);

	@Autowired
	private CurrencyQueryRepository currencyQueryRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.history.services.HistoryServices#
	 * getLastQueriesOf(int, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getLastQueriesOf(int number, String userName) {
		try {
			verifyPositiveNumber(number);
			verifyUserName(userName);
			return this.currencyQueryRepository.getLastQueriesOf(number, userName);

		} catch (Exception e) {
			logger.error("Exception in getLastQueriesOf with the following parameters: number: '{}', userName: '{}'", new Object[] { number, userName });
			throw new HistoryServiceException(String.format("Exception in getLastQueriesOf: %s", e.getMessage()), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pabloam.microservices.converter.history.services.HistoryServices#save
	 * (java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public Boolean save(String provider, String userName, Map<String, Object> currencyQuery) {
		try {

			verifyProvider(provider);
			verifyUserName(userName);
			verifyCurrencyQuery(currencyQuery);

			this.currencyQueryRepository.saveCurrencyQuery(userName, provider, currencyQuery);
			return true;

		} catch (Exception e) {
			logger.error("Exception saving with the following parameters: provider: '{}', userName: '{}', currencyQuery: '{}'",
					new Object[] { provider, userName, currencyQuery });
			throw new HistoryServiceException(String.format("Exception saving currencyQuery: %s", e.getMessage()), e);
		}

	}

	/**
	 * Verifies that the currencyQuery map is not null nor empty
	 * 
	 * @param currencyQuery
	 */
	private void verifyCurrencyQuery(Map<String, Object> currencyQuery) {
		if (CollectionUtils.isEmpty(currencyQuery)) {
			throw new IllegalArgumentException("The currency query map cannot be null nor empty.");
		}
	}

	/**
	 * Verifies that the userName is not null nor empty
	 * 
	 * @param userName
	 */
	private void verifyUserName(String userName) {
		if (!StringUtils.hasText(userName)) {
			throw new IllegalArgumentException("The userName cannot be null nor empty.");
		}
	}

	/**
	 * Verifies taht the provider is not null nor empty
	 * 
	 * @param provider
	 */
	private void verifyProvider(String provider) {
		if (!StringUtils.hasText(provider)) {
			throw new IllegalArgumentException("The provider cannot be null nor empty.");
		}
	}

	/**
	 * Verifies that the number is positive
	 * 
	 * @param number
	 */
	private void verifyPositiveNumber(int number) {
		if (number <= 0) {
			throw new IllegalArgumentException("The number must be positive.");
		}
	}

}
