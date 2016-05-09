package com.pabloam.microservices.converter.provider.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.provider.services.ProviderServices;

@RestController
public class ProviderController {

	// The logger
	final Logger logger = (Logger) LoggerFactory.getLogger(ProviderController.class);

	/**
	 * The provider services implemented an exchange provider
	 */
	@Autowired
	private ProviderServices providerServices;

//	@Autowired
//	private PublishingServices publishingServices;

	private ConcurrentLinkedQueue<Map<String, Object>> memory;

	@PostConstruct
	private void init() {
		this.memory = new ConcurrentLinkedQueue<>();
	}

	/**
	 * Returns the current rates for the given sourceCurrency and the given
	 * expectedCurrencies
	 * 
	 * @param user
	 * @param sourceCurrency
	 * @param expectedCurrencies
	 * @return
	 */
	@RequestMapping(value = "provider/getCurrent/{user}/{sourceCurrency}/{expectedCurrencies}")
	public Map<String, Object> getCurrentRates(@PathVariable String user, @PathVariable String sourceCurrency, @PathVariable String[] expectedCurrencies) {
		Map<String, Object> response = providerServices.getCurrentRates(sourceCurrency, expectedCurrencies);
		storeResponseInMemory(user, response);
		return response;
	}

	/**
	 * Returns the historical rates for the given sourceCurrency and the given
	 * expectedCurrencies
	 * 
	 * @param user
	 * @param date
	 * @param sourceCurrency
	 * @param expectedCurrencies
	 * @return
	 */
	@RequestMapping(value = "provider/getHistorical/{user}/{date}/{sourceCurrency}/{expectedCurrencies}")
	public Map<String, Object> getCurrentRates(@PathVariable String user, @PathVariable String date, @PathVariable String sourceCurrency,
			@PathVariable String[] expectedCurrencies) {
		Map<String, Object> response = providerServices.getHistoricalRates(sourceCurrency, date, expectedCurrencies);
		storeResponseInMemory(user, response);
		return response;
	}

	/**
	 * @param user
	 * @param response
	 */
	private void storeResponseInMemory(String user, Map<String, Object> response) {
		Map<String, Object> savedResponse = new HashMap<String, Object>();
		savedResponse.put("user", user);
		savedResponse.put("provider", providerServices.getProviderName());
		savedResponse.put("response", response);
		this.memory.add(response);
	}

	// @Scheduled(fixedRate = 5000)
//	private void publishMemory() {
//
//		while (!this.memory.iterator().hasNext()) {
//
//			Map<String, Object> map = this.memory.peek();
//
//			String user = (String) map.get("user");
//			String provider = (String) map.get("provider");
//			@SuppressWarnings("unchecked")
//			Map<String, Object> response = (Map<String, Object>) map.get("response");
//
//			try {
//				this.publishingServices.publishInHistoryServices(user, provider, response);
//				this.memory.poll();
//			} catch (Exception e) {
//				logger.error("Publish Memory: {}", e.getMessage(), e);
//			}
//		}
//	}

	// Error Handling
	@ExceptionHandler(Exception.class)
	public @ResponseBody Map<String, String> handleRestException(HttpServletRequest request, Exception ex) {
		logger.error(ex.getMessage(), ex);
		return Collections.singletonMap("error", String.format("'%s' - message: '%s'", request.getRequestURL().toString(), ex.getMessage()));
	}
}
