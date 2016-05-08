package com.pabloam.microservices.converter.provider.web;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.provider.services.ProviderServices;

@RestController
@EnableResourceServer
public class ProviderController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(ProviderController.class);

	/**
	 * The provider services implemented an exchange provider
	 */
	@Autowired
	private ProviderServices providerServices;

	/**
	 * Returns the current rates for the given sourceCurrency and the given
	 * expectedCurrencies
	 * 
	 * @param sourceCurrency
	 * @param expectedCurrencies
	 * @return
	 */
	@RequestMapping(value = "provider/getCurrent/{sourceCurrency}/{expectedCurrencies}")
	public Map<String, Object> getCurrentRates(@PathVariable String sourceCurrency, @PathVariable String[] expectedCurrencies) {
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
	public Map<String, Object> getCurrentRates(@PathVariable String date, @PathVariable String sourceCurrency, @PathVariable String[] expectedCurrencies) {
		return providerServices.getHistoricalRates(sourceCurrency, date, expectedCurrencies);
	}

	// Error Handling
	@ExceptionHandler(Exception.class)
	public @ResponseBody Map<String, String> handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		return Collections.singletonMap("error",
				String.format("Exception in request: '%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage()));
	}
}
