package com.pabloam.microservices.converter.history.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.history.model.CurrencyQuery;
import com.pabloam.microservices.converter.history.services.HistoryServices;

@RestController
@EnableResourceServer
public class HistoryRestController {

	@Autowired
	private HistoryServices historyServices;

	/**
	 * Returns the last number queries for the given user
	 * 
	 * @return
	 */
	@RequestMapping(value = "/history/getLast", method = RequestMethod.GET)
	public List<CurrencyQuery> getLastQueriesOf(@RequestParam("n") int number, @RequestParam("u") String userName) {
		return historyServices.getLastQueriesOf(number, userName);
	}

	/**
	 * Returns true if the query has been stored
	 * 
	 * @param provider
	 * @param userName
	 * @param currencyQuery
	 * @return
	 */
	@RequestMapping(value = "history/save", method = RequestMethod.POST)
	public Boolean saveQuery(@RequestParam("p") String provider, @RequestParam("u") String userName, @RequestBody Map<String, Object> currencyQuery) {
		return historyServices.save(provider, userName, currencyQuery);
	}

}
