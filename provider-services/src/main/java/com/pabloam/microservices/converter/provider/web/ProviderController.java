package com.pabloam.microservices.converter.provider.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.common.ConvertedResponse;
import com.pabloam.microservices.converter.common.RefreshIntervalEnum;
import com.pabloam.microservices.converter.common.exceptions.JsonException;
import com.pabloam.microservices.converter.provider.services.ProviderServices;

import rx.Observable;

@RestController
public class ProviderController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(ProviderController.class);

	/**
	 * The provider services implemented an exchange provider
	 */
	@Autowired
	private ProviderServices providerServices;

	/**
	 * Returns the name of the provider implementing the provider services
	 * 
	 * @return
	 */
	@RequestMapping(value = "/provider/getName", method = RequestMethod.GET)
	public String getName() {
		return providerServices.getProviderName();
	}

	/**
	 * Returns the refresh interval for the given provider
	 * 
	 * @return
	 */
	@RequestMapping(value = "/provider/getRefreshInterval", method = RequestMethod.GET)
	public RefreshIntervalEnum getRefreshInterval() {
		return providerServices.getRefreshInterval();
	}

	/**
	 * Returns the current rates for the given sourceCurrency and the given
	 * expectedCurrencies
	 * 
	 * @param sourceCurrency
	 * @param expectedCurrencies
	 * @return
	 */
	@RequestMapping(value = "provider/getCurrent/{sourceCurrency}/{expectedCurrencies}")
	public ConvertedResponse getCurrentRates(@PathVariable String sourceCurrency, @PathVariable String[] expectedCurrencies) {
		return providerServices.getCurrentRates(sourceCurrency, expectedCurrencies);
	}

	/**
	 * Returns the historical rates for the given sourceCurrency and the given
	 * expectedCurrencies
	 * 
	 * @param date
	 * @param sourceCurrency
	 * @param expectedCurrencies
	 * @return
	 */
	@RequestMapping(value = "provider/getHistorical/{date}/{sourceCurrency}/{expectedCurrencies}")
	public ConvertedResponse getCurrentRates(@PathVariable String date, @PathVariable String sourceCurrency,
			@PathVariable String[] expectedCurrencies) {
		return providerServices.getHistoricalRates(sourceCurrency, date, expectedCurrencies);
	}

	// Error Handling
	@ExceptionHandler(Exception.class)
	public @ResponseBody String handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		return String.format("Exception in request: '%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage());
	}
}
