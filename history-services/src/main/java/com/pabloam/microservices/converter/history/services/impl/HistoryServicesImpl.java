package com.pabloam.microservices.converter.history.services.impl;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.pabloam.microservices.converter.history.exceptions.HistoryServiceException;
import com.pabloam.microservices.converter.history.model.CurrencyQuery;
import com.pabloam.microservices.converter.history.model.Quote;
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
	public List<CurrencyQuery> getLastQueriesOf(int number, String userName) {
		try {
			verifyPositiveNumber(number);
			verifyUserName(userName);
			return this.currencyQueryRepository.getLastQueriesOf(number, userName);

		} catch (Exception e) {
			logger.error("Exception in getLastQueriesOf with the following parameters: number: '{}', email: '{}'", new Object[] { number, userName });
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
	public Boolean save(String provider, String userName, Map<String, Object> originalQuery) {
		try {

			verifyProvider(provider);
			verifyUserName(userName);
			verifyCurrencyQuery(originalQuery);

			CurrencyQuery currencyQuery = createFromOriginal(originalQuery);
			currencyQuery.setEmail(userName);
			currencyQuery.setProvider(provider);
			currencyQuery.setCreated(Instant.now(Clock.systemUTC()).toEpochMilli());
			currencyQuery.setDeleted(false);
			
			this.currencyQueryRepository.saveCurrencyQuery(currencyQuery);
			return true;

		} catch (Exception e) {
			logger.error("Exception saving with the following parameters: provider: '{}', userName: '{}', currencyQuery: '{}'",
					new Object[] { provider, userName, originalQuery });
			throw new HistoryServiceException(String.format("Exception saving currencyQuery: %s", e.getMessage()), e);
		}

	}

	private CurrencyQuery createFromOriginal(Map<String, Object> originalQuery) {
		CurrencyQuery q = new CurrencyQuery();
		q.setEmail((String) originalQuery.get("email"));
		q.setSource((String) originalQuery.get("source"));
		q.setTimestamp(((Integer) originalQuery.get("timestamp")).longValue());
		q.setHistorical((Boolean) originalQuery.get("historical"));
		q.setDate((String) originalQuery.get("date"));
		@SuppressWarnings("unchecked")
		Map<String, Double> originalQuotes = (Map<String, Double>) originalQuery.get("quotes");

		q.setQuotes(originalQuotes.entrySet().stream().map(m -> toQuote(m.getKey(), m.getValue())).collect(Collectors.toList()));
		return q;
	}

	private Quote toQuote(String s, Double d) {
		return new Quote(s, d);
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
