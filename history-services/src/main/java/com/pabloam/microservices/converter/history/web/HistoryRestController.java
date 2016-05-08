package com.pabloam.microservices.converter.history.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pabloam.microservices.converter.history.services.HistoryServices;

import rx.Observable;

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
	@RequestMapping(value = "/history/getLast/{number}/{username}", method = RequestMethod.GET)
	public Observable<List<Map<String, Object>>> getLastQueriesOf(@PathVariable int number, @PathVariable String userName) {
		return Observable.just(historyServices.getLastQueriesOf(number, userName));
	}

	@RequestMapping(value = "history/save/{username}/{provider}", method = RequestMethod.POST)
	public Observable<Boolean> saveQuery(@PathVariable String provider, @PathVariable String userName, Map<String, Object> currencyQuery) {
		return Observable.just(historyServices.save(provider, userName, currencyQuery));
	}

}
